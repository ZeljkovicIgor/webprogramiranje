package services;

import java.util.ArrayList;
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
import beans.Restaurant;
import beans.RestaurantCategory;
import beans.User;
import beans.UserRole;
import dao.ItemDAO;
import dao.RestaurantDAO;
import dao.UserDAO;

@Path("/restaurant")
public class RestaurantService {

	@Context
	ServletContext context;
	
	public RestaurantService() {
		super();
	}

	@PostConstruct
	public void init() {

		if (context.getAttribute("restaurantDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("restaurantDAO", new RestaurantDAO(contextPath));
		}
		if (context.getAttribute("userDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (context.getAttribute("itemDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("itemDAO", new ItemDAO(contextPath));
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurant(@Context HttpServletRequest request, Restaurant restaurant) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		
		Restaurant saved = restaurantDAO.save(restaurant);
		
		return Response.ok(saved).build();
	}
	
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editRestaurant(@Context HttpServletRequest request, Restaurant restaurant) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		
		Restaurant saved = restaurantDAO.save(restaurant);
		
		return Response.ok(saved).build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurant(@Context HttpServletRequest request, @PathParam("id") Long id) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(401).entity("Nije dozvoljeno!").build();
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		
		Restaurant deleted = restaurantDAO.delete(id);
		
		if(deleted == null)
			return Response.status(400).entity("Nije moguce obrisati").build();
		
		for(Item item : deleted.getDrinks()) {
			item.setDeleted(true);
			itemDAO.save(item);
		}
		
		for(Item item : deleted.getFoods()) {
			item.setDeleted(true);
			itemDAO.save(item);
		}
		
		return Response.ok(deleted).build();
	}
	
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchRestaurant(@Context HttpServletRequest request, Restaurant restaurant) {
		if(!restaurant.getRestaurantCategory().equals(RestaurantCategory.NULL) && restaurant.getAddress().equals("") && restaurant.getName().equals("")) {
			
		}else {
			User logged = (User) request.getSession().getAttribute("logged");
			if(logged==null)
				return Response.status(401).entity("Nije dozvoljeno!").build();
		}
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		
		if(restaurant.getAddress().equals("") && restaurant.getName().equals("") && restaurant.getRestaurantCategory() == RestaurantCategory.NULL)
			return Response.status(400).entity("Nisu odabrani parametri za pretragu!").build();
		
		List<Restaurant> restaurants = restaurantDAO.search(restaurant);
		List<Restaurant> returnRest = new ArrayList<>();
		
		for(Restaurant r : restaurants) {
			if(!r.isDeleted())
				returnRest.add(r);
		}
		
		return Response.ok(returnRest).build();
	}
	
	@POST
	@Path("/addFavorite/{restaurantId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFavoriteRestaurant(@Context HttpServletRequest request, @PathParam("restaurantId") Long restaurantId) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.BUYER)
			return Response.status(401).entity("Nije dozvoljeno!").build();
		
		RestaurantDAO restaurantDAO = (RestaurantDAO) context.getAttribute("restaurantDAO");
		Restaurant restaurant = restaurantDAO.findOne(restaurantId);
		
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		User user = userDAO.findOne(logged.getId());
		System.out.println("Korisnik: " + user.toString());
		
		for(Restaurant r : user.getFavRestaurants()) {
			if(r.getId() == restaurant.getId())
				return Response.status(400).entity("Neuspesno!").build();
		}
		
		user.getFavRestaurants().add(restaurant);
		
		User savedUser = userDAO.save(user);
		
		if(savedUser == null)
			return Response.status(400).entity("Neuspesno!").build();
		
		return Response.ok(restaurant).build();
	}
	
	@GET
	@Path("/favorite")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFavorite(@Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.BUYER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		List<Restaurant> favs = logged.getFavRestaurants();
		
		return Response.ok(favs).build();
	}
	
	
}
