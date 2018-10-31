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

import beans.User;

public class UserDAO {

	private HashMap<Long, User> users = new HashMap<Long, User>();
	private String contextPath;

	public UserDAO() {
		
	}
	
	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		load();
	}
	
	public boolean usernameExists(String name) {
		for(User u : users.values()) {
			if(u.getUsername().equals(name) && !u.isDeleted())
				return true;
		}
		return false;
	}
	
	public User save(User user) {
		if(user.getId()==null) {						//	insert
			Long userId = (long) users.size();
			userId++;
			user.setId(userId);
			
			users.put(userId, user);
			save();
			System.out.println("INSERT " + user.toString());
			return user;
		}else {											//	update
			User oldUser = users.get(user.getId());
			if(oldUser.isDeleted()) {
				System.out.println("User with the ID = " + oldUser.getId() + "is deleted!\n");
				return null;
			}
			users.put(user.getId(), user);
			save();
			System.out.println("UPDATE " + user.toString());
			return user;
		}
		
	}
	
	public User findOne(Long userId) {
		User user = users.get(userId);
		if(user.isDeleted()) {
			System.out.println("User with the ID = " + user.getId() + "is deleted!\n");
			return null;
		}else {
			System.out.println("SELECT " + user.toString());
			return user;
		}
	}
	
	public List<User> findAll(){
		List<User> returnUsers = new ArrayList<>();
		List<User> users = new ArrayList<>(this.users.values());
		
		for(int i = 0; i < users.size(); i++) {
			if(!users.get(i).isDeleted()) {
				System.out.println("SELECT " + users.get(i).toString());
				returnUsers.add(users.get(i));
			}
				
		}
		
		return returnUsers;
		
	}
	
	public User delete(Long userId) {
		User user = findOne(userId);
		if(user != null) {
			user.setDeleted(true);
			save();
			System.out.println("DELETE " + user.toString());
			return user;
		}else {
			return null;
		}
	}
	
	private void save() {
		ObjectMapper mapper = new ObjectMapper();
		List<User> users = new ArrayList<User>(this.users.values());
		try {
			File file = new File (this.contextPath + "/users.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void load() {
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		List<User> users = new ArrayList<>();
		try {
			File file = new File (contextPath + "/users.json");
			reader = new BufferedReader(new FileReader(file));
			
			if (reader != null) {
				users = mapper.readValue(reader, new TypeReference<List<User>>(){});
				this.users.clear();
				
				for (User v : users) {
					this.users.put(v.getId(), v);
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
