package dto;

import java.util.List;

import beans.OrderStatus;

public class OrderDTO {

	private Long id;
	private String note;
	private String orderDateTime;
	private String address;
	private Long buyerId;
	private Long delivererId;
	private OrderStatus status;
	private List<Long> orderedItemsIds;
	private List<Integer> orderedItemsQuantity;
	
	public OrderDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public List<Long> getOrderedItemsIds() {
		return orderedItemsIds;
	}

	public void setOrderedItemsIds(List<Long> orderedItemsIds) {
		this.orderedItemsIds = orderedItemsIds;
	}

	public List<Integer> getOrderedItemsQuantity() {
		return orderedItemsQuantity;
	}

	public void setOrderedItemsQuantity(List<Integer> orderedItemsQuantity) {
		this.orderedItemsQuantity = orderedItemsQuantity;
	}
	
}
