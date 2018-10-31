package services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Item;
import beans.Order;
import beans.OrderStatus;
import beans.User;
import beans.UserRole;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dto.OrderDTO;

@Path("/order")
public class OrderService {

	@Context
	ServletContext context;
	
	public OrderService() {
		super();
	}

	@PostConstruct
	public void init() {

		if (context.getAttribute("orderDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("orderDAO", new OrderDAO(contextPath));
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
	@Path("/addOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrder(@Context HttpServletRequest request, OrderDTO orderDTO) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.BUYER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		ItemDAO itemDAO = (ItemDAO) context.getAttribute("itemDAO");
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("orderDAO");
		
		Order order = new Order();
		order.setAddress(orderDTO.getAddress());
		order.setBuyerId(logged.getId());
		order.setNote(orderDTO.getNote());
		order.setOrderDateTime(orderDTO.getOrderDateTime());
		order.setStatus(OrderStatus.ORDERED);
		
		List<Item> orderedItems = new ArrayList<>();
		for (int i = 0; i < orderDTO.getOrderedItemsIds().size(); i++) {
			Item item = itemDAO.findOne(orderDTO.getOrderedItemsIds().get(i));
			item.setQuantity(item.getQuantity() + orderDTO.getOrderedItemsQuantity().get(i));
			orderedItems.add(item);
			itemDAO.save(item);
		}
		
		order.setOrderedItems(orderedItems);
		Order saved = orderDAO.save(order);
		
		return Response.ok(saved).build();
	}
	
	@GET
	@Path("/history")
	public Response getHistory(@Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.BUYER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		List<Order> orders = logged.getBuyerOrders();
		
		return Response.ok(orders).build();
	}
	
	@GET
	@Path("/undelivered")
	public Response getUndelivered(@Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.DELIVERER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("orderDAO");
		
		List<Order> orders = orderDAO.findAll();
		List<Order> undelivered = new ArrayList<>();
		
		for(Order order : orders) {
			if(order.getStatus() == OrderStatus.ORDERED || order.getDelivererId() == null)
				undelivered.add(order);
		}
		
		return Response.ok(undelivered).build();
	}
	
	@POST
	@Path("/deliver/{orderId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deliverOrder(@PathParam("orderId") Long orderId, @Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.DELIVERER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("orderDAO");
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		for(Order order : logged.getDelivererOrders()) {
			if(order.getStatus() == OrderStatus.DELIVERING)
				return Response.status(400).entity("Trenutno dostavljate porudzbinu!").build();
		}
		
		Order order = orderDAO.findOne(orderId);
		order.setDelivererId(logged.getId());
		order.setStatus(OrderStatus.DELIVERING);
		logged.getDelivererOrders().add(order);
		Order thisOrder = orderDAO.save(order);
		userDAO.save(logged);
		
		return Response.ok(thisOrder).build();
	}
	
	@GET
	@Path("/delivering")
	public Response getCurrentlyDelivering(@Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.DELIVERER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("orderDAO");
		
		List<Order> orders = orderDAO.findAll();
		
		for(Order order : orders) {
			if(order.getStatus() == OrderStatus.DELIVERING)
				return Response.ok(order).build();
		}
		
		return Response.status(417).entity("Ne dostavljate nista trenutno.").build();
		
	}
	
	@POST
	@Path("/delivered/{orderId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setDelivered(@PathParam("orderId") Long orderId, @Context HttpServletRequest request) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.DELIVERER)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("orderDAO");
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		for(Order order : logged.getDelivererOrders()) {
			if(order.getStatus() == OrderStatus.DELIVERING) {
				order.setStatus(OrderStatus.DELIVERED);
				orderDAO.save(order);
				userDAO.save(logged);
				return Response.ok(order).build();
			}
		}
		
		return Response.status(400).build();
	}
	
	
}
