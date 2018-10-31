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

import beans.Restaurant;
import beans.RestaurantCategory;

public class RestaurantDAO {

	private HashMap<Long, Restaurant> restaurants = new HashMap<Long, Restaurant>();
	private String contextPath;

	public RestaurantDAO() {
		
	}
	
	public RestaurantDAO(String contextPath) {
		this.contextPath = contextPath;
		load();
	}
	
	public List<Restaurant> search(Restaurant restaurant){
		List<Restaurant> restaurants = new ArrayList<>(this.restaurants.values());
		List<Restaurant> searchRestaurants = new ArrayList<>();
		
		if(!restaurant.getName().equals("") && !restaurant.getAddress().equals("") && restaurant.getRestaurantCategory() != RestaurantCategory.NULL) {
			for(Restaurant r : restaurants) {
				if(r.getAddress().contains(restaurant.getAddress()) && r.getName().contains(restaurant.getName()) && restaurant.getRestaurantCategory().equals(r.getRestaurantCategory()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else if(!restaurant.getName().equals("") && !restaurant.getAddress().equals("")) {
			for(Restaurant r : restaurants) {
				if(r.getAddress().contains(restaurant.getAddress()) && r.getName().contains(restaurant.getName()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else if(!restaurant.getAddress().equals("") && restaurant.getRestaurantCategory() != RestaurantCategory.NULL) {
			for(Restaurant r : restaurants) {
				if(r.getAddress().contains(restaurant.getAddress()) && restaurant.getRestaurantCategory().equals(r.getRestaurantCategory()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else if(!restaurant.getName().equals("") && restaurant.getRestaurantCategory() != RestaurantCategory.NULL) {
			for(Restaurant r : restaurants) {
				if(r.getName().contains(restaurant.getName()) && restaurant.getRestaurantCategory().equals(r.getRestaurantCategory()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else if(!restaurant.getName().equals("")) {
			for(Restaurant r : restaurants) {
				if(r.getName().contains(restaurant.getName()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else if(!restaurant.getAddress().equals("")) {
			for(Restaurant r : restaurants) {
				if(r.getAddress().contains(restaurant.getAddress()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}else {
			for(Restaurant r : restaurants) {
				if(restaurant.getRestaurantCategory().equals(r.getRestaurantCategory()) && !restaurant.isDeleted())
					searchRestaurants.add(r);
			}
			return searchRestaurants;
		}
		
	}
	
	public Restaurant save(Restaurant restaurant) {
		if(restaurant.getId()==null) {						//	insert
			Long restaurantId = (long) restaurants.size();
			restaurantId++;
			restaurant.setId(restaurantId);
			
			restaurants.put(restaurantId, restaurant);
			save();
			System.out.println("INSERT " + restaurant.toString());
			return restaurant;
		}else {											//	update
			Restaurant oldRestaurant = restaurants.get(restaurant.getId());
			if(oldRestaurant.isDeleted()) {
				System.out.println("Restaurant with the ID = " + oldRestaurant.getId() + "is deleted!\n");
				return null;
			}
			
			if(!restaurant.getDrinks().isEmpty() && !restaurant.getFoods().isEmpty()) {
				oldRestaurant.setAddress(restaurant.getAddress());
				oldRestaurant.setName(restaurant.getName());
				oldRestaurant.setRestaurantCategory(restaurant.getRestaurantCategory());
				
				save();
				System.out.println("UPDATE " + oldRestaurant.toString());
				return oldRestaurant;
			}else {
				restaurants.put(restaurant.getId(), restaurant);
				
				save();
				System.out.println("UPDATE " + oldRestaurant.toString());
				return restaurant;
			}
		}
		
	}
	
	public Restaurant findOne(Long restaurantId) {
		Restaurant restaurant = restaurants.get(restaurantId);
		if(restaurant.isDeleted()) {
			System.out.println("Restaurant with the ID = " + restaurant.getId() + "is deleted!\n");
			return null;
		}else {
			System.out.println("SELECT " + restaurant.toString());
			return restaurant;
		}
	}
	
	public List<Restaurant> findAll(){
		List<Restaurant> returnRestaurants = new ArrayList<>();
		List<Restaurant> restaurants = new ArrayList<>(this.restaurants.values());
		
		for(int i = 0; i < restaurants.size(); i++) {
			if(!restaurants.get(i).isDeleted()) {
				System.out.println("SELECT " + restaurants.get(i).toString());
				returnRestaurants.add(restaurants.get(i));
			}
				
		}
		
		return returnRestaurants;
		
	}
	
	public Restaurant delete(Long restaurantId) {
		Restaurant restaurant = findOne(restaurantId);
		if(restaurant != null) {
			restaurant.setDeleted(true);
			save();
			System.out.println("DELETE " + restaurant.toString());
			return restaurant;
		}else {
			return null;
		}
	}
	
	private void save() {
		ObjectMapper mapper = new ObjectMapper();
		List<Restaurant> restaurants = new ArrayList<Restaurant>(this.restaurants.values());
		try {
			File file = new File (this.contextPath + "/restaurants.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , restaurants);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void load() {
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Restaurant> restaurants = new ArrayList<>();
		try {
			File file = new File (contextPath + "/restaurants.json");
			reader = new BufferedReader(new FileReader(file));
			
			if (reader != null) {
				restaurants = mapper.readValue(reader, new TypeReference<List<Restaurant>>(){});
				this.restaurants.clear();
				
				for (Restaurant v : restaurants) {
					this.restaurants.put(v.getId(), v);
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
