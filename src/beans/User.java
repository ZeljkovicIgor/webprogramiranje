package beans;

import java.util.ArrayList;
import java.util.List;

public class User {

	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private UserRole role;
	private String phoneNumber;
	private String email;
	private String registrationDate;
	
	private List<Order> buyerOrders;
	private List<Restaurant> favRestaurants;
	
	private Vehicle vehicle;
	private List<Order> delivererOrders;
	
	private boolean deleted;
	
	public User() {
		super();
		favRestaurants = new ArrayList<>();
		buyerOrders = new ArrayList<>();
		delivererOrders = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", role=" + role + ", phoneNumber=" + phoneNumber + ", email=" + email
				+ ", registrationDate=" + registrationDate + ", buyerOrders=" + buyerOrders + ", favRestaurants="
				+ favRestaurants + ", vehicle=" + vehicle + ", delivererOrders=" + delivererOrders + ", deleted="
				+ deleted + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<Order> getBuyerOrders() {
		return buyerOrders;
	}

	public void setBuyerOrders(List<Order> buyerOrders) {
		this.buyerOrders = buyerOrders;
	}

	public List<Restaurant> getFavRestaurants() {
		return favRestaurants;
	}

	public void setFavRestaurants(List<Restaurant> favRestaurants) {
		this.favRestaurants = favRestaurants;
	}

	public List<Order> getDelivererOrders() {
		return delivererOrders;
	}

	public void setDelivererOrders(List<Order> delivererOrders) {
		this.delivererOrders = delivererOrders;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
