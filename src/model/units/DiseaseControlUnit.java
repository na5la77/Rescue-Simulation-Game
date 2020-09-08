package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		super(unitID, location, stepsPerCycle, worldListener);

	}

	public void treat() {
		this.getTarget().getDisaster().setActive(false);
		Citizen a = (Citizen) this.getTarget();

		int tox = a.getToxicity();

		if (tox != 0) {
			tox -= this.getTreatmentAmount();
			a.setToxicity(tox);
		} else {
			a.setState(CitizenState.RESCUED);
			super.heal();
		}
		if (a.getState().equals(CitizenState.DECEASED)) {
			this.jobsDone();
		}
	}

}
