package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public abstract class MedicalUnit extends Unit {

	private int healingAmount;
	private int treatmentAmount;// Read

	public MedicalUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {

		super(unitID, location, stepsPerCycle, worldListener);
		healingAmount = 10;
		treatmentAmount = 10;

	}

	public int getTreatmentAmount() {
		return treatmentAmount;
	}

	public abstract void treat();

	public void heal() {
		Citizen a = (Citizen) this.getTarget();
		int hp = a.getHp();
		if (!a.getState().equals(CitizenState.DECEASED)) {
			hp += this.healingAmount;
			a.setHp(hp);
			if (a.getHp() == 100) {
				this.jobsDone();
			}
		}
	}

}
