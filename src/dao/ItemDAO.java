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

import beans.Item;
import beans.ItemType;

public class ItemDAO {

	private HashMap<Long, Item> items = new HashMap<Long, Item>();
	private String contextPath;

	public ItemDAO() {
		
	}
	
	public ItemDAO(String contextPath) {
		this.contextPath = contextPath;
		load();
	}
	
	public List<Item> search(Item item){
		List<Item> items = new ArrayList<>(this.items.values());
		List<Item> searchItems = new ArrayList<>();
		
		if(item.getItemType() != ItemType.NULL && !item.getName().equals("") && item.getPrice() != 0 && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getPrice() == r.getPrice() && item.getName().equals(r.getName()) && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && !item.getName().equals("") && item.getPrice() != 0) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getPrice() == r.getPrice() && item.getName().equals(r.getName()) && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && !item.getName().equals("") && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getName().equals(r.getName()) && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && item.getPrice() != 0 && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getPrice() == r.getPrice() && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(!item.getName().equals("") && item.getPrice() != 0 && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getPrice() == r.getPrice() && item.getName().equals(r.getName()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && !item.getName().equals("")) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getName().equals(r.getName()) && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && item.getPrice() != 0) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getPrice() == r.getPrice() && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(!item.getName().equals("") && item.getPrice() != 0) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getPrice() == r.getPrice() && item.getName().equals(r.getName()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(!item.getName().equals("") && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getName().equals(r.getName()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getPrice() != 0 && item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId() && item.getPrice() == r.getPrice())
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getItemType() != ItemType.NULL) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getItemType().equals(r.getItemType()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(!item.getName().equals("")) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getName().equals(r.getName()))
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getPrice() != 0) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getPrice() == r.getPrice())
					searchItems.add(r);
			}
			return searchItems;
		}else if(item.getRestaurantId() == null) {
			for(Item r : items) {
				if(!r.isDeleted() && item.getRestaurantId() == r.getRestaurantId())
					searchItems.add(r);
			}
			return searchItems;
		}else {
			return null;
		}
		
	}
	
	public Item save(Item item) {
		if(item.getId()==null) {						//	insert
			Long itemId = (long) items.size();
			itemId++;
			item.setId(itemId);
			
			items.put(itemId, item);
			save();
			System.out.println("INSERT " + item.toString());
			return item;
		}else {											//	update
			Item oldItem = items.get(item.getId());
			if(oldItem.isDeleted()) {
				System.out.println("Item with the ID = " + oldItem.getId() + "is deleted!\n");
				return null;
			}
			items.put(item.getId(), item);
			save();
			System.out.println("UPDATE " + item.toString());
			return item;
		}
		
	}
	
	public Item findOne(Long itemId) {
		Item item = items.get(itemId);
		if(item.isDeleted()) {
			System.out.println("Item with the ID = " + item.getId() + "is deleted!\n");
			return null;
		}else {
			System.out.println("SELECT " + item.toString());
			return item;
		}
	}
	
	public List<Item> findAll(){
		List<Item> returnItems = new ArrayList<>();
		List<Item> items = new ArrayList<>(this.items.values());
		
		for(int i = 0; i < items.size(); i++) {
			if(!items.get(i).isDeleted()) {
				System.out.println("SELECT " + items.get(i).toString());
				returnItems.add(items.get(i));
			}
				
		}
		
		return returnItems;
		
	}
	
	public Item delete(Long itemId) {
		Item item = findOne(itemId);
		if(item != null) {
			item.setDeleted(true);
			save();
			System.out.println("DELETE " + item.toString());
			return item;
		}else {
			return null;
		}
	}
	
	private void save() {
		ObjectMapper mapper = new ObjectMapper();
		List<Item> items = new ArrayList<Item>(this.items.values());
		try {
			File file = new File (this.contextPath + "/items.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , items);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void load() {
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Item> items = new ArrayList<>();
		try {
			File file = new File (contextPath + "/items.json");
			reader = new BufferedReader(new FileReader(file));
			
			if (reader != null) {
				items = mapper.readValue(reader, new TypeReference<List<Item>>(){});
				this.items.clear();
				
				for (Item v : items) {
					this.items.put(v.getId(), v);
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
