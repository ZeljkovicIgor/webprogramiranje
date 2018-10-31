package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Vehicle;

public class VehicleDAO {

	private HashMap<Long, Vehicle> vehicles = new HashMap<Long, Vehicle>();
	private String contextPath;

	public VehicleDAO() {
		
	}
	
	public VehicleDAO(String contextPath) {
		this.contextPath = contextPath;
		load();
	}
	
	public Vehicle save(Vehicle vehicle) {
		if(vehicle.getId()==null) {						//	insert
			Long vehicleId = (long) vehicles.size();
			vehicleId++;
			vehicle.setId(vehicleId);
			
			vehicles.put(vehicleId, vehicle);
			save();
			System.out.println("INSERT " + vehicle.toString());
			return vehicle;
		}else {											//	update
			Vehicle oldVehicle = vehicles.get(vehicle.getId());
			if(oldVehicle.isDeleted()) {
				System.out.println("Vehicle with the ID = " + oldVehicle.getId() + "is deleted!\n");
				return null;
			}
			vehicles.put(vehicle.getId(), vehicle);
			save();
			System.out.println("UPDATE " + vehicle.toString());
			return vehicle;
		}
		
	}
	
	public Vehicle findOne(Long vehicleId) {
		Vehicle vehicle = vehicles.get(vehicleId);
		if(vehicle.isDeleted()) {
			System.out.println("Vehicle with the ID = " + vehicle.getId() + "is deleted!\n");
			return null;
		}else {
			System.out.println("SELECT " + vehicle.toString());
			return vehicle;
		}
	}
	
	public List<Vehicle> findAll(){
		List<Vehicle> returnVehicles = new ArrayList<>();
		List<Vehicle> vehicles = new ArrayList<>(this.vehicles.values());
		
		for(int i = 0; i < vehicles.size(); i++) {
			if(!vehicles.get(i).isDeleted()) {
				System.out.println("SELECT " + vehicles.get(i).toString());
				returnVehicles.add(vehicles.get(i));
			}
				
		}
		
		return returnVehicles;
		
	}
	
	public Vehicle delete(Long vehicleId) {
		Vehicle vehicle = findOne(vehicleId);
		if(vehicle != null) {
			vehicle.setDeleted(true);
			save();
			System.out.println("DELETE " + vehicle.toString());
			return vehicle;
		}else {
			return null;
		}
	}
	
	private void save() {
		ObjectMapper mapper = new ObjectMapper();
		List<Vehicle> vehicles = new ArrayList<Vehicle>(this.vehicles.values());
		try {
			File file = new File (this.contextPath + "/vehicles.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , vehicles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Vehicle> vehicles = new ArrayList<>();
		try {
			File file = new File (contextPath + "/vehicles.json");
			reader = new BufferedReader(new FileReader(file));
			
			if (reader != null) {
				vehicles = mapper.readValue(reader, new TypeReference<List<Vehicle>>(){});
				this.vehicles.clear();
				
				for (Vehicle v : vehicles) {
					this.vehicles.put(v.getId(), v);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
}
