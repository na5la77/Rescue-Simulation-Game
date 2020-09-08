package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		super(unitID, location, stepsPerCycle, worldListener);

	}

	public void treat() {
		this.getTarget().getDisaster().setActive(false);
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		a.getDisaster().setActive(false);
		int fire = a.getFireDamage();

		int newFire = fire - 10;
		if (fire != 0) {
			a.setFireDamage(newFire);
		}
		if (fire == 0) {
			this.setState(UnitState.IDLE);
		}
		if (a.getStructuralIntegrity() == 0 || a.getFireDamage() == 0) {
			this.jobsDone();
		}
	}

	/*
	 * public void jobsDone() { ResidentialBuilding a = (ResidentialBuilding)
	 * this.getTarget(); int x = a.getLocation().getX(); int y =
	 * a.getLocation().getY(); if (a.getFireDamage() == 0 ||
	 * a.getStructuralIntegrity() == 0) { this.setState(UnitState.IDLE);
	 * this.getWorldListener().assignAddress(this, x, y); } }
	 */

}
