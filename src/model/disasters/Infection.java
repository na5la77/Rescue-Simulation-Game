package model.disasters;

import model.people.Citizen;

public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		if (this.isActive()) {
			Citizen a = (Citizen) this.getTarget();
			int tox = a.getToxicity();
			int newTox = tox + 15;
			a.setToxicity(newTox);
		}

	}

	public void strike() {
		// this.setActive(true);
		Citizen a = (Citizen) this.getTarget();
		// a.struckBy(this);
		int tox = a.getToxicity();
		int newTox = tox + 25;
		a.setToxicity(newTox);

	}

}
