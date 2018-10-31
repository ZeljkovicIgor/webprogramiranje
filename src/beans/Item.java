package beans;

public class Item {

	private Long id;
	private int amount;
	private ItemType itemType;
	private boolean deleted;
	private Long restaurantId;
	private String name;
	private String description;
	private double price;
	private int quantity;

	public Item() {
		super();
	}
	
	

	@Override
	public String toString() {
		return "Item [id=" + id + ", amount=" + amount + ", itemType=" + itemType + ", deleted=" + deleted
				+ ", restaurantId=" + restaurantId + ", name=" + name + ", description=" + description + ", price="
				+ price + ", quantity=" + quantity + "]";
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
