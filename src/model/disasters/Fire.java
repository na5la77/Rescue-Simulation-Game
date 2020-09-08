package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		if (this.isActive()) {
			ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
			int fireDamage = a.getFireDamage();
			int newFireDamage = fireDamage + 10;
			a.setFireDamage(newFireDamage);
		}

	}

	public void strike() {

		// this.setActive(true);
		ResidentialBuilding a = (ResidentialBuilding) this.getTarget();
		// a.struckBy(this);
		int fireDamage = a.getFireDamage();
		int newFireDamage = fireDamage + 10;
		a.setFireDamage(newFireDamage);

	}

}
