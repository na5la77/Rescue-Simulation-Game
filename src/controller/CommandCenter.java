package controller;

import java.util.ArrayList;

import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;

public class CommandCenter implements SOSListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;

	public CommandCenter() throws Exception {

		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = new ArrayList<Unit>();

	}

	public void receiveSOSCall(Rescuable r) {
		if (r instanceof Citizen) {
			Citizen a = (Citizen) r;
			visibleCitizens.add(a);
		} else if (r instanceof ResidentialBuilding) {
			ResidentialBuilding a = (ResidentialBuilding) r;
			visibleBuildings.add(a);
		}

	}
}
