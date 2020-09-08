package model.units;

import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {

	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;// Write
	private int stepsPerCycle;
	private WorldListener worldListener;// R..W

	public Unit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;

	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;

	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	public void cycleStep() {
		if (this.getState() != UnitState.IDLE) {
			if (this instanceof Evacuator) {
				System.out.println(distanceToTarget);
				Evacuator ev = (Evacuator) this;
				ResidentialBuilding a = (ResidentialBuilding) this.target;
				boolean flag = true;
				
				if (!ev.getPassengers().isEmpty()&& ev.getDistanceToBase()!=0) {

					ev.setDistanceToBase(ev.getDistanceToBase() - ev.getStepsPerCycle());
					if (ev.getDistanceToBase() <= 0) {
						ev.setDistanceToBase(0);
						ev.setDistanceToTarget(calculateDistance(this.target));
						worldListener.assignAddress(this, 0, 0);
						ev.treat();
					}
					
				} else if (this.distanceToTarget == 0) {
					int x = this.target.getLocation().getX();
					int y = this.target.getLocation().getY();
					ev.setDistanceToBase(x + y);
					this.worldListener.assignAddress(ev, x, y);
					this.treat();
				} else  if(ev.getPassengers().isEmpty()){
					this.distanceToTarget -= stepsPerCycle;
					if(distanceToTarget<=0) {
						int x = this.target.getLocation().getX();
						int y = this.target.getLocation().getY();
						ev.setDistanceToBase(x + y);
						distanceToTarget=0;
					}

				}

			} else {
				if (this.state.equals(UnitState.RESPONDING)) {
					this.distanceToTarget = this.distanceToTarget - this.stepsPerCycle;
					if (distanceToTarget <= 0) {
						this.distanceToTarget = 0;
						int x = this.target.getLocation().getX();
						int y = this.target.getLocation().getY();
						worldListener.assignAddress(this, x, y);

					}
				} else if (this.distanceToTarget == 0) {
					this.treat();
				}
			}
		}
	}

	public abstract void treat();

	public int calculateDistance(Rescuable r) {
		this.target = r;
		Address unitLocation = this.location;
		int unitLocationX = unitLocation.getX();
		int unitLocationY = unitLocation.getY();
		Address targetLocation = this.target.getLocation();
		int targetLocationX = targetLocation.getX();
		int targetLocationY = targetLocation.getY();
		int deltaX = (int) Math.abs(unitLocationX - targetLocationX);
		int deltaY = (int) Math.abs(unitLocationY - targetLocationY);
		int newDistanceToTarget = deltaX + deltaY;

		return newDistanceToTarget;
	}

	public void respond(Rescuable r) {
		if (r != null) {
			if (!(this.state.equals(UnitState.TREATING))) {
				this.state = UnitState.RESPONDING;
				this.target = r;
				this.distanceToTarget = calculateDistance(r);
			} else if ((!(this.target instanceof Citizen))) {

				this.target.getDisaster().setActive(true);
				this.target = r;
				this.distanceToTarget = calculateDistance(r);
				this.setState(UnitState.RESPONDING);

			} else {
				Citizen a = (Citizen) r;
				if (!(a.getState().equals(CitizenState.RESCUED))) {
					this.target.getDisaster().setActive(true);
					this.target = r;
					this.distanceToTarget = calculateDistance(r);
					this.setState(UnitState.RESPONDING);
				} else {
					this.target = r;
					this.distanceToTarget = calculateDistance(r);
					this.setState(UnitState.RESPONDING);
				}
			}
		}

	}

	public void jobsDone() {
		this.setState(UnitState.IDLE);
		this.target = null;
	}

}
