package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		super(unitID, location, stepsPerCycle, worldListener);

	}

	public void treat() {
		this.getTarget().getDisaster().setActive(false);
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		a.getDisaster().setActive(false);
		int gas = a.getGasLevel();

		int newGas = gas - 10;
		if (gas != 0) {
			a.setGasLevel(newGas);
		}
		if (a.getStructuralIntegrity() == 0 || a.getGasLevel() == 0) {
			this.jobsDone();
		}

	}

	/*
	 * public void jobsDone() { ResidentialBuilding a = (ResidentialBuilding)
	 * this.getTarget(); int x = a.getLocation().getX(); int y =
	 * a.getLocation().getY(); if (a.getGasLevel() == 0 ||
	 * a.getStructuralIntegrity() == 0) { this.setState(UnitState.IDLE);
	 * this.getWorldListener().assignAddress(this, x, y); } }
	 */

}
