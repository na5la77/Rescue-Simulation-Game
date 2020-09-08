package model.infrastructure;

import java.util.ArrayList;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Rescuable, Simulatable {

	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private SOSListener emergencyService; // Write

	public ResidentialBuilding(Address location) {

		this.location = location;
		this.structuralIntegrity = 100;
		occupants = new ArrayList<Citizen>();

	}

	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}

	public void setStructuralIntegrity(int structuralIntegrity) {
		this.structuralIntegrity = structuralIntegrity;
		if (structuralIntegrity <= 0) {
			this.structuralIntegrity = 0;
			for (int i = 0; i < occupants.size(); i++) {
				occupants.get(i).setHp(0);
			}
		}
	}

	public int getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(int fireDamage) {
		this.fireDamage = fireDamage;
		if (fireDamage < 0) {
			this.fireDamage = 0;
		} else if (fireDamage > 100) {
			this.fireDamage = 100;
		}

	}

	public int getGasLevel() {
		return gasLevel;
	}

	public void setGasLevel(int gasLevel) {
		this.gasLevel = gasLevel;
		if (gasLevel < 0) {
			this.gasLevel = 0;
		} else if (gasLevel >= 100) {
			this.gasLevel = 100;
			for (int i = 0; i < occupants.size(); i++) {
				occupants.get(i).setHp(0);
			}
		}
	}

	public int getFoundationDamage() {
		return foundationDamage;
	}

	public void setFoundationDamage(int foundationDamage) {
		this.foundationDamage = foundationDamage;
		if (foundationDamage >= 100) {
			this.foundationDamage = 100;
			this.structuralIntegrity = 0;
		}
	}

	public Address getLocation() {
		return location;
	}

	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public void cycleStep() {
		int decrease = (int) ((Math.random() * 6) + 5);
		int foundation = this.foundationDamage;
		int fire = this.fireDamage;
		if (foundation > 0) {
			this.structuralIntegrity -= decrease;
		}
		if (fire > 0 && fire < 30) {
			this.structuralIntegrity -= 3;
		} else if (fire >= 30 && fire < 70) {
			this.structuralIntegrity -= 5;
		} else if (fire >= 70 && fire < 100) {
			this.structuralIntegrity -= 7;
		}

	}

	public void struckBy(Disaster d) {
		this.disaster = d;
		this.disaster.setActive((true));
		this.emergencyService.receiveSOSCall(this);

	}

}
