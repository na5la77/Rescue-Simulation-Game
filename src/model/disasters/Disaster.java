package model.disasters;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable {

	private int startCycle;
	private Rescuable target;
	private boolean active;

	public Disaster(int startCycle, Rescuable target) {

		this.startCycle = startCycle;
		this.target = target;

	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}

	public boolean validTarget(Rescuable target) {
		if (target instanceof Citizen) {
			Citizen a = (Citizen) target;
			if (a.getState().equals(CitizenState.DECEASED)) {
				return false;
			} else
				return true;
		} else if (target instanceof ResidentialBuilding) {
			ResidentialBuilding b = (ResidentialBuilding) target;
			if (b.getStructuralIntegrity() == 0) {
				return false;
			
			} else
				return true;
		} else
			return true;
	}

	public void strike() {
		if (this.validTarget(target)) {
			active = true;
			target.struckBy(this);
			
		}
	}

	public abstract void cycleStep();

}
