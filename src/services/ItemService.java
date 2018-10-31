package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Item;
import beans.ItemType;
import beans.Restaurant;
import beans.User;
import beans.UserRole;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.RestaurantDAO;
import dao.UserDAO;

@Path("item")
public class ItemService {

	@Context
	ServletContext context;
	
	public ItemService() {
		super();
	}

	@PostConstruct
	public void init() {

		if (context.getAttribute("itemDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("itemDAO", new ItemDAO(contextPath));
		}
		if (context.getAttribute("userDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (context.getAttribute("restaurantDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("restaurantDAO", new RestaurantDAO(contextPath));
		}
		if (context.getAttribute("orderDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("orderDAO", new OrderDAO(contextPath));
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addItem(@Context HttpServletRequest request, Item item) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		Restaurant restaurant = restaurantDAO.findOne(item.getRestaurantId());
		
		Item saved = itemDAO.save(item);
		
		if(saved == null) {
			return Response.status(400).entity("Greska!").build();
		}
		
		if(saved.getItemType() == ItemType.DRINK) {
			restaurant.getDrinks().add(saved);
			restaurantDAO.save(restaurant);
		}else {
			restaurant.getFoods().add(saved);
			restaurantDAO.save(restaurant);
		}
		
		return Response.ok(saved).build();
	}
	
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editItem(@Context HttpServletRequest request, Item item) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		Restaurant restaurant = restaurantDAO.findOne(item.getRestaurantId());
		
		Item saved = itemDAO.save(item);
		
		if(saved == null) {
			return Response.status(400).entity("Greska!").build();
		}
		
		if(saved.getItemType() == ItemType.DRINK) {
			List<Item> drinks = restaurant.getDrinks();
			for (int i = 0; i < drinks.size(); i++) {
				if(drinks.get(i).getId() == saved.getId()) {
					drinks.remove(i);
					drinks.add(saved);
				}
			}
			restaurantDAO.save(restaurant);
		}else {
			List<Item> foods = restaurant.getFoods();
			for (int i = 0; i < foods.size(); i++) {
				if(foods.get(i).getId() == saved.getId()) {
					foods.remove(i);
					foods.add(saved);
				}
			}
			restaurantDAO.save(restaurant);
		}
		
		return Response.ok(saved).build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteItem(@Context HttpServletRequest request, @PathParam("id") Long id) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(401).entity("Nije dozvoljeno!").build();
		
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		
		Item deleted = itemDAO.delete(id);
		
		if(deleted == null)
			return Response.status(400).entity("Nije moguce obrisati").build();
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		Restaurant restaurant = restaurantDAO.findOne(deleted.getRestaurantId());
		
		if(deleted.getItemType() == ItemType.DRINK) {
			List<Item> drinks = restaurant.getDrinks();
			for (int i = 0; i < drinks.size(); i++) {
				if(drinks.get(i).getId() == deleted.getId()) {
					drinks.remove(i);
				}
			}
			restaurantDAO.save(restaurant);
		}else {
			List<Item> foods = restaurant.getFoods();
			for (int i = 0; i < foods.size(); i++) {
				if(foods.get(i).getId() == deleted.getId()) {
					foods.remove(i);
				}
			}
			restaurantDAO.save(restaurant);
		}
		
		return Response.ok(deleted).build();
	}
	
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchItem(Item item) {
		
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		
		if(item.getRestaurantId() == null && item.getName().equals("") && item.getItemType() == ItemType.NULL && item.getPrice() == 0)
			return Response.status(400).entity("Nisu odabrani parametri za pretragu!").build();
		
		List<Item> items = itemDAO.search(item);
		
		return Response.ok(items).build();
	}
	
	@GET
	@Path("/mostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMostPopular() {
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		List<Item> topTen = itemDAO.findAll();
		
		Comparator<Item> comparator = (Item item1, Item item2)-> Integer.valueOf(item1.getQuantity()).compareTo(item2.getQuantity());
		
		topTen.sort(comparator.reversed());
		
		if (topTen.size() >= 10) {
			List<Item> newList = topTen.subList(0, 10);
			return Response.ok(newList).build();
		} else {
			return Response.ok(topTen).build();
		}
		
				
	}
	
	@GET
	@Path("/inRestaurant/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInRestaurant(@PathParam("id") Long id) {
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		List<Item> items = itemDAO.findAll();
		List<Item> returnItems = new ArrayList<>();
		for(Item item : items) {
			if(item.getRestaurantId() == id)
				returnItems.add(item);
		}
		
		return Response.ok(returnItems).build();
	}
	
}
