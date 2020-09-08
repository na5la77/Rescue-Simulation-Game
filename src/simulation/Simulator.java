package simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;

public class Simulator implements WorldListener {

	private int currentCycle;
	private ArrayList<ResidentialBuilding> buildings;
	private ArrayList<Citizen> citizens;
	private ArrayList<Unit> emergencyUnits;// Read
	private ArrayList<Disaster> plannedDisasters;
	private ArrayList<Disaster> executedDisasters;
	private Address[][] world;
	private SOSListener emergencyService; // Write

	public Simulator(SOSListener emergencyService) throws Exception {

		buildings = new ArrayList<ResidentialBuilding>();
		citizens = new ArrayList<Citizen>();
		emergencyUnits = new ArrayList<Unit>();
		plannedDisasters = new ArrayList<Disaster>();
		executedDisasters = new ArrayList<Disaster>();
		this.emergencyService= emergencyService;
		currentCycle = 0;

		world = new Address[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				world[i][j] = new Address(i, j);
			}
		}

		loadUnits("units.csv");
		loadBuildings("buildings.csv");
		loadCitizens("citizens.csv");
		loadDisasters("disasters.csv");

		for (int i = 0; i < buildings.size(); i++) {

			ResidentialBuilding building = buildings.get(i);
			for (int j = 0; j < citizens.size(); j++) {

				Citizen citizen = citizens.get(j);
				if (citizen.getLocation() == building.getLocation())
					building.getOccupants().add(citizen);

			}
		}
	}

	private void loadUnits(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			String id = info[1];
			int steps = Integer.parseInt(info[2]);

			switch (info[0]) {

			case "AMB":
				emergencyUnits.add(new Ambulance(id, world[0][0], steps, this));
				break;

			case "DCU":
				emergencyUnits.add(new DiseaseControlUnit(id, world[0][0], steps, this));
				break;

			case "EVC":
				emergencyUnits.add(new Evacuator(id, world[0][0], steps, this, Integer.parseInt(info[3])));
				break;

			case "FTK":
				emergencyUnits.add(new FireTruck(id, world[0][0], steps, this));
				break;

			case "GCU":
				emergencyUnits.add(new GasControlUnit(id, world[0][0], steps, this));
				break;

			}

			line = br.readLine();
		}

		br.close();
	}

	private void loadBuildings(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			ResidentialBuilding a= new ResidentialBuilding(world[x][y]);
			buildings.add(a);
			a.setEmergencyService(emergencyService);
			line = br.readLine();

		}
		br.close();
	}

	private void loadCitizens(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			String id = info[2];
			String name = info[3];
			int age = Integer.parseInt(info[4]);
			Citizen a = new Citizen(world[x][y], id, name, age, this);
			a.setEmergencyService(emergencyService);
			citizens.add(a);
			line = br.readLine();

		}
		br.close();
	}

	private void loadDisasters(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int startCycle = Integer.parseInt(info[0]);
			ResidentialBuilding building = null;
			Citizen citizen = null;

			if (info.length == 3)
				citizen = getCitizenByID(info[2]);
			else {

				int x = Integer.parseInt(info[2]);
				int y = Integer.parseInt(info[3]);
				building = getBuildingByLocation(world[x][y]);

			}

			switch (info[1]) {

			case "INJ":
				plannedDisasters.add(new Injury(startCycle, citizen));
				break;

			case "INF":
				plannedDisasters.add(new Infection(startCycle, citizen));
				break;

			case "FIR":
				plannedDisasters.add(new Fire(startCycle, building));
				break;

			case "GLK":
				plannedDisasters.add(new GasLeak(startCycle, building));
				break;
			}

			line = br.readLine();
		}
		br.close();
	}

	private Citizen getCitizenByID(String id) {

		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getNationalID().equals(id))
				return citizens.get(i);
		}

		return null;
	}

	private ResidentialBuilding getBuildingByLocation(Address location) {

		for (int i = 0; i < buildings.size(); i++) {
			if (buildings.get(i).getLocation() == location)
				return buildings.get(i);
		}

		return null;
	}

	public void assignAddress(Simulatable sim, int x, int y) {
		Address newLocation = world[x][y];
		if (sim instanceof Citizen) {
			Citizen a = (Citizen) sim;
			a.setLocation(newLocation);
		} else if (sim instanceof Unit) {
			Unit a = (Unit) sim;
			a.setLocation(newLocation);

		}

	}

	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public boolean checkGameOver() {
		if (!(plannedDisasters.isEmpty())) {
			return false;
		} else {
			for (int i = 0; i < executedDisasters.size(); i++) {
				if (executedDisasters.get(i).isActive() == true) {
					return false;
				}
			}
			for (int i = 0; i < emergencyUnits.size(); i++) {
				if (!(emergencyUnits.get(i).getState().equals(UnitState.IDLE)))
					return false;
			}

		}
		return true;
	}

	/*
	 * { boolean disasterFlag = false; for (int i = 0; i < executedDisasters.size();
	 * i++) { if (!(this.executedDisasters.get(i).isActive())) { disasterFlag =
	 * true; } else { disasterFlag = false; } } boolean unitFlag = false; for (int i
	 * = 0; i < emergencyUnits.size(); i++) { if
	 * (this.emergencyUnits.get(i).getState().equals(UnitState.IDLE)) { unitFlag =
	 * true; } else { unitFlag = false; } }
	 * 
	 * if (plannedDisasters.isEmpty() && disasterFlag == true && unitFlag == true) {
	 * return true; } else return false; }
	 */

	public int calculateCasualties() {

		int dead = 0;
		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getState().equals(CitizenState.DECEASED)) {
				dead++;
			}
		}
		return dead;
	}

	public void nextCycle()

	{
		currentCycle++;
		Disaster a;

		for (int i = 0; i < plannedDisasters.size(); i++) {
			a = plannedDisasters.get(i);
			if (a.getStartCycle() == currentCycle) {
				if (a.getTarget() instanceof ResidentialBuilding) {
					ResidentialBuilding building = (ResidentialBuilding) a.getTarget();
					//building.setEmergencyService(emergencyService);
					int gas = building.getGasLevel();
					if (gas == 0) {
						//building.struckBy(a);
						a.strike();
						executedDisasters.add(plannedDisasters.remove(i));

					} else if (gas > 0 && gas < 70) {
						if (a instanceof Fire) {
							building.getDisaster().setActive(false);
							building.setFireDamage(0);
							Disaster col = new Collapse(currentCycle, building);
							executedDisasters.add(col);
							//building.struckBy(col);
							col.strike();

						} else {
							//building.struckBy(a);
							a.strike();
							executedDisasters.add(plannedDisasters.remove(i));
						}
					} else if (gas >= 70) {
						if (a instanceof Fire) {
							building.setStructuralIntegrity(0);
						} else {
							//building.struckBy(a);
							a.strike();
							executedDisasters.add(plannedDisasters.remove(i));
						}
					} else if (a instanceof GasLeak) {
						if (building.getFireDamage() > 0) {
							building.getDisaster().setActive(false);
							building.setFireDamage(0);
							Disaster col = new Collapse(currentCycle, building);
							executedDisasters.add(col);
							//building.struckBy(col);
							col.strike();
						} else {
							//building.struckBy(a);
							a.strike();
							executedDisasters.add(plannedDisasters.remove(i));
						}
					} else {
						//building.struckBy(a);
						a.strike();
						executedDisasters.add(plannedDisasters.remove(i));
					}

				}
				
			}
		}
		int bs = buildings.size();
		for (int i = 0; i < buildings.size(); i++) {
			ResidentialBuilding building = buildings.get(i);
			if (building.getFireDamage() >= 100) {
				Disaster col = new Collapse(currentCycle, building);
				executedDisasters.add(col);
				col.strike();
			}
		}
		int es = emergencyUnits.size();
		for (int i = 0; i < emergencyUnits.size(); i++) {
			if(emergencyUnits.get(i) instanceof Evacuator) {
				Evacuator ev= (Evacuator) emergencyUnits.get(i);
				ev.setDistanceToBase(ev.getLocation().getX()+ev.getLocation().getY());
				//ev.setDistanceToTarget(Math.abs(ev.getLocation().getX()-ev.getTarget().getLocation().getX())+Math.abs(ev.getLocation().getY()-ev.getTarget().getLocation().getY()));
			}
			emergencyUnits.get(i).cycleStep();
			
		}
		Disaster e;
		int ex = executedDisasters.size();
		for (int i = 0; i < executedDisasters.size(); i++) {
			e = executedDisasters.get(i);
			if (e.isActive() && e.getStartCycle() != currentCycle) {
				e.cycleStep();
			}
		}
		for (int i = 0; i < buildings.size(); i++) {
			buildings.get(i).cycleStep();
			buildings.get(i).setEmergencyService(emergencyService);
		}
		for (int i = 0; i < citizens.size(); i++) {
			
			citizens.get(i).cycleStep();
			citizens.get(i).setEmergencyService(emergencyService);
		}
		
		this.checkGameOver();

	}

}
