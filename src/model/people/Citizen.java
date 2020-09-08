package model.people;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class Citizen implements Rescuable, Simulatable {

	private CitizenState state;
	private Disaster disaster;
	private String name;
	private String nationalID;
	private int age;
	private int hp;
	private int bloodLoss;
	private int toxicity;
	private Address location;
	private SOSListener emergencyService; // Write
	private WorldListener worldListener; // R..W

	public Citizen(Address location, String nationalID, String name, int age, WorldListener worldListener) {

		this.name = name;
		this.nationalID = nationalID;
		this.age = age;
		this.location = location;
		this.state = CitizenState.SAFE;
		this.hp = 100;
		this.worldListener = worldListener;

	}

	public CitizenState getState() {
		return state;
	}

	public void setState(CitizenState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
		if (hp > 100) {
			this.hp = 100;
		} else if (hp <= 0) {
			this.hp = 0;
			this.state = CitizenState.DECEASED;
		}

	}

	public int getBloodLoss() {
		return bloodLoss;
	}

	public void setBloodLoss(int bloodLoss) {
		this.bloodLoss = bloodLoss;
		if (bloodLoss >= 100) {
			this.bloodLoss = 100;
			this.hp = 0;
			this.state = CitizenState.DECEASED;
		} else if (bloodLoss < 0) {
			this.bloodLoss = 0;
		}
		if (bloodLoss == 0) {
			this.state = CitizenState.RESCUED;
		}
	}

	public int getToxicity() {
		return toxicity;
	}

	public void setToxicity(int toxicity) {
		this.toxicity = toxicity;
		if (toxicity > 100) {
			this.toxicity = 100;
			this.hp = 0;
			this.state = CitizenState.DECEASED;

		} else if (toxicity < 0) {
			this.toxicity = 0;
		}
		if (toxicity == 0) {
			this.state = CitizenState.RESCUED;
		}
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public String getNationalID() {
		return nationalID;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public void cycleStep() {

		int blood = this.bloodLoss;
		int tox = this.toxicity;
		if (this.state != CitizenState.DECEASED) {
			if ((blood > 0 && blood < 30) || (tox > 0 && tox < 30)) {
				this.hp = this.hp - 5;
			} else if ((blood >= 30 && blood < 70) || (tox >= 30 && tox < 70)) {
				this.hp = this.hp - 10;
			} else if ((blood >= 70 && blood < 100) || (tox >= 70 && tox < 100))
				this.hp = this.hp - 15;
		}

	}

	public void struckBy(Disaster d) {
		this.disaster = d;
		this.disaster.setActive(true);
		this.state = CitizenState.IN_TROUBLE;
		this.emergencyService.receiveSOSCall(this);

	}

}
