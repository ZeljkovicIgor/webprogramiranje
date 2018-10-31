package services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import beans.UserRole;
import dao.UserDAO;

@Path("/user")
public class UserService {

	@Context
	ServletContext context;
	
	public UserService() {
		super();
	}

	@PostConstruct
	public void init() {

		if (context.getAttribute("userDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@GET
	@Path("/isLogged")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isLogged(@Context HttpServletRequest request) {
		User logged = (User) request.getSession().getAttribute("logged");
		
		return Response.ok(logged).build();
		
	}
	
	@GET
	@Path("/loggout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loggout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
		
		return Response.ok("Uspesno izlogovan!").build();
		
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User userFront) {
		
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		User user = new User();
		
		user.setEmail(userFront.getEmail());
		user.setFirstName(userFront.getFirstName());
		user.setLastName(userFront.getLastName());
		user.setPassword(userFront.getPassword());
		user.setPhoneNumber(userFront.getPhoneNumber());
		user.setUsername(userFront.getUsername());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String regDate = sdf.format(Calendar.getInstance().getTime());
		user.setRegistrationDate(regDate);
		
		user.setRole(UserRole.BUYER);
		
		if(userDAO.usernameExists(user.getUsername()))
			return Response.status(409).entity("Korisnicko ime je zauzeto").build();
		
		return Response.ok(userDAO.save(user)).build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		List<User> users = userDAO.findAll();
		
		for(User u : users) {
			if(user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				HttpSession newSession = request.getSession();
				newSession.setAttribute("logged", u);
				System.out.println("Ulogovan korisnik " + u.toString());
				return Response.ok(u).build();
			}
		}
		
		return Response.status(400).entity("Pogresno korisnicko ime ili lozinka!").build();
	}
	
	@POST
	@Path("/changeRole")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeRole(User user, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) context.getAttribute("userDAO");
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		List<User> users = userDAO.findAll();
		for(User u : users) {
			if(u.getUsername().equals(user.getUsername())) {
				u.setRole(user.getRole());
				userDAO.save(u);
				return Response.ok(u).build();
			}
		}
		
		return Response.status(400).entity("Doslo je do greske!").build();
	}
	
}
