package beans;

import java.util.List;

public class Order {

	private Long id;
	private String note;
	private String orderDateTime;
	private String address;
	private Long buyerId;
	private Long delivererId;
	private OrderStatus status;
	private List<Item> orderedItems;
	private boolean deleted;
	
	public Order() {
		super();
	}
	
	

	@Override
	public String toString() {
		return "Order [id=" + id + ", note=" + note + ", orderDateTime=" + orderDateTime + ", address=" + address
				+ ", buyerId=" + buyerId + ", delivererId=" + delivererId + ", status=" + status + ", orderedItems="
				+ orderedItems + ", deleted=" + deleted + "]";
	}



	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public List<Item> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<Item> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getDelivererId() {
		return delivererId;
	}

	public void setDelivererId(Long delivererId) {
		this.delivererId = delivererId;
	}

	public OrderStatus getStatus() {
		return status;
	}
	
}
