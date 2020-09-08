package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		super(unitID, location, stepsPerCycle, worldListener);

	}

	public void treat() {

		Citizen a = (Citizen) this.getTarget();
		if (a != null && !(a.getState().equals(CitizenState.DECEASED))) {
			a.getDisaster().setActive(false);
			int blood = a.getBloodLoss();
			if (blood != 0) {

				blood -= this.getTreatmentAmount();
				a.setBloodLoss(blood);
			} else {
				a.setState(CitizenState.RESCUED);
				super.heal();
			}
			if (a.getState().equals(CitizenState.DECEASED)) {
				this.jobsDone();
			}

		}
	}
}
