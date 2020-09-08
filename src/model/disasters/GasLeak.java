package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		if (this.isActive()) {
			ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
			int gasLevel = a.getGasLevel();
			int newGasLevel = gasLevel + 15;
			a.setGasLevel(newGasLevel);
		}
	}

	public void strike() {
		// this.setActive(true);
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		// a.struckBy(this);
		int gasLevel = a.getGasLevel();
		int newGasLevel = gasLevel + 10;
		a.setGasLevel(newGasLevel);

	}

}
