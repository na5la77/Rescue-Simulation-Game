package model.disasters;

import model.people.Citizen;

public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		if (this.isActive()) {
			Citizen a = (Citizen) this.getTarget();
			int bloodLoss = a.getBloodLoss();
			int newBloodLoss = bloodLoss + 10;
			a.setBloodLoss(newBloodLoss);
		}

	}

	public void strike() {
		// this.setActive(true);
		Citizen a = (Citizen) this.getTarget();
		// a.struckBy(this);
		int bloodLoss = a.getBloodLoss();
		int newBloodLoss = bloodLoss + 30;
		a.setBloodLoss(newBloodLoss);

	}

}
