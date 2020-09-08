
package model.units;

import java.util.ArrayList;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {

		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	public void treat() {
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		int distanceToTarget = this.calculateDistance(a);
		if(getDistanceToBase()==0) {
			while(!this.getPassengers().isEmpty()) {
				Citizen ci=this.getPassengers().remove(0);
				ci.getWorldListener().assignAddress(ci, 0, 0);
				ci.setState(CitizenState.RESCUED);
			}
		} 
		else if (distanceToTarget == 0) {
			this.setState(UnitState.TREATING);
			int s = a.getOccupants().size();
			for (int i = 0; i < this.getMaxCapacity() && !a.getOccupants().isEmpty(); i++) {
				Citizen b= a.getOccupants().remove(0);
				this.getPassengers().add(b);
			}
		}
		
//		if (distanceToTarget == 0 && a.getOccupants().isEmpty() && this.getPassengers().isEmpty()) {
//			this.jobsDone();
//		}
	}
	public static void main(String[] args) {
		ArrayList  a=new ArrayList();
		a.add(10);
		a.add(20);
		a.add(13);
		a.add(14);
		a.add(15);
		while(!a.isEmpty()) {
			System.out.println((int)a.remove(0));
		}
	}
	/*
	 * ResidentialBuilding a = (ResidentialBuilding) this.getTarget(); int
	 * distanceToTarget = this.calculateDistance(a); int x = a.getLocation().getX();
	 * int y = a.getLocation().getY(); int newDistanceToBase = x + y; //int
	 * totalMoved; int distanceToBase = this.getDistanceToBase(); if
	 * (distanceToTarget == 0) { //this.setState(UnitState.TREATING); for (int i =
	 * 0; i <= this.getMaxCapacity() && i < a.getOccupants().size(); i++) {
	 * this.getPassengers().add(a.getOccupants().remove(i)); }
	 * this.setState(UnitState.RESPONDING); } if
	 * (this.getState().equals(UnitState.RESPONDING)) {
	 * this.setDistanceToBase(newDistanceToBase - this.getStepsPerCycle()); if
	 * (this.getDistanceToBase() == 0) { int s = this.getPassengers().size(); for
	 * (int i = 0; i < s; i++) { Citizen c = this.getPassengers().remove(i);
	 * c.setState(CitizenState.RESCUED); c.getWorldListener().assignAddress(c, 0,
	 * 0); } } } if (this.getPassengers().isEmpty() &&
	 * a.getOccupants().isEmpty()&&distanceToTarget == 0) { this.jobsDone(); }
	 * 
	 * } /* ResidentialBuilding a = (ResidentialBuilding) this.getTarget(); int
	 * distanceToTarget = this.calculateDistance(a); int x = a.getLocation().getX();
	 * int y = a.getLocation().getY(); int newDistanceToBase = x + y;
	 * this.setDistanceToBase(newDistanceToBase); // ArrayList<Citizen> resc =
	 * this.getPassengers(); if (distanceToTarget == 0) {
	 * this.setState(UnitState.TREATING); for (int i = 0; i <= this.getMaxCapacity()
	 * && !(a.getOccupants().isEmpty()); i++) {
	 * this.getPassengers().add(a.getOccupants().remove(i)); } } else if
	 * (this.getDistanceToBase() == 0 && !(this.getPassengers().isEmpty())) { for
	 * (int i = 0; i < this.getMaxCapacity(); i++) { Citizen cit =
	 * this.getPassengers().remove(i); this.getWorldListener().assignAddress(cit, 0,
	 * 0); cit.setState(CitizenState.RESCUED); }
	 * this.setState(UnitState.RESPONDING);
	 * this.setDistanceToBase(newDistanceToBase); } else if
	 * (a.getOccupants().isEmpty() && distanceToTarget == 0) {
	 * this.setState(UnitState.IDLE); this.getWorldListener().assignAddress(this, x,
	 * y); }
	 * 
	 * }
	 */

	/*
	 * public void jobsDone() { ResidentialBuilding a = (ResidentialBuilding)
	 * this.getTarget(); int distanceToTarget = this.calculateDistance(a); int x =
	 * a.getLocation().getX(); int y = a.getLocation().getY(); if (distanceToTarget
	 * == 0 && (a.getOccupants().isEmpty() || a.getStructuralIntegrity() == 0)) {
	 * this.setState(UnitState.IDLE); this.getWorldListener().assignAddress(this, x,
	 * y); } }
	 */

}
