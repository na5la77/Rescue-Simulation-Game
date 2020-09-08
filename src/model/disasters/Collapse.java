package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		if (this.isActive()) {
			ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
			int foundationDamage = a.getFoundationDamage();
			int newFoundationDamage = foundationDamage + 10;
			a.setFoundationDamage(newFoundationDamage);
		}
	}

	public void strike() {
		// this.setActive(true);
		//super.strike();
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		// a.struckBy(this);
		int foundationDamage = a.getFoundationDamage();
		int newFoundationDamage = foundationDamage + 10;
		a.setFoundationDamage(newFoundationDamage);

	}

}
