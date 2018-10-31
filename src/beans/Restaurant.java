package beans;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

	private Long id;
	private String name;
	private String address;
	private List<Item> foods;
	private List<Item> drinks;
	private RestaurantCategory restaurantCategory;
	private boolean deleted;
	
	public Restaurant() {
		super();
		foods = new ArrayList<>();
		drinks = new ArrayList<>();
	}
	
	

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", address=" + address + ", foods=" + foods + ", drinks="
				+ drinks + ", restaurantCategory=" + restaurantCategory + ", deleted=" + deleted + "]";
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Item> getFoods() {
		return foods;
	}

	public void setFoods(List<Item> foods) {
		this.foods = foods;
	}

	public List<Item> getDrinks() {
		return drinks;
	}

	public void setDrinks(List<Item> drinks) {
		this.drinks = drinks;
	}

	public RestaurantCategory getRestaurantCategory() {
		return restaurantCategory;
	}

	public void setRestaurantCategory(RestaurantCategory restaurantCategory) {
		this.restaurantCategory = restaurantCategory;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
