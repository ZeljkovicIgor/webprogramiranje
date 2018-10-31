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

import beans.Order;

public class OrderDAO {

	private HashMap<Long, Order> orders = new HashMap<Long, Order>();
	private String contextPath;

	public OrderDAO() {
		
	}
	
	public OrderDAO(String contextPath) {
		this.contextPath = contextPath;
		load();
	}
	
	public Order save(Order order) {
		if(order.getId()==null) {						//	insert
			Long orderId = (long) orders.size();
			orderId++;
			order.setId(orderId);
			
			orders.put(orderId, order);
			save();
			System.out.println("INSERT " + order.toString());
			return order;
		}else {											//	update
			Order oldOrder = orders.get(order.getId());
			if(oldOrder.isDeleted()) {
				System.out.println("Order with the ID = " + oldOrder.getId() + "is deleted!\n");
				return null;
			}
			orders.put(order.getId(), order);
			save();
			System.out.println("UPDATE " + order.toString());
			return order;
		}
		
	}
	
	public Order findOne(Long orderId) {
		Order order = orders.get(orderId);
		if(order.isDeleted()) {
			System.out.println("Order with the ID = " + order.getId() + "is deleted!\n");
			return null;
		}else {
			System.out.println("SELECT " + order.toString());
			return order;
		}
	}
	
	public List<Order> findAll(){
		List<Order> returnOrders = new ArrayList<>();
		List<Order> orders = new ArrayList<>(this.orders.values());
		
		for(int i = 0; i < orders.size(); i++) {
			if(!orders.get(i).isDeleted()) {
				System.out.println("SELECT " + orders.get(i).toString());
				returnOrders.add(orders.get(i));
			}
				
		}
		
		return returnOrders;
		
	}
	
	public Order delete(Long orderId) {
		Order order = findOne(orderId);
		if(order != null) {
			order.setDeleted(true);
			save();
			System.out.println("DELETE " + order.toString());
			return order;
		}else {
			return null;
		}
	}
	
	private void save() {
		ObjectMapper mapper = new ObjectMapper();
		List<Order> orders = new ArrayList<Order>(this.orders.values());
		try {
			File file = new File (this.contextPath + "/orders.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , orders);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void load() {
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Order> orders = new ArrayList<>();
		try {
			File file = new File (contextPath + "/orders.json");
			reader = new BufferedReader(new FileReader(file));
			
			if (reader != null) {
				orders = mapper.readValue(reader, new TypeReference<List<Order>>(){});
				this.orders.clear();
				
				for (Order v : orders) {
					this.orders.put(v.getId(), v);
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
