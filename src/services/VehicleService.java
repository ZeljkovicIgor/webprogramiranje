package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import beans.UserRole;
import beans.Vehicle;
import dao.UserDAO;
import dao.VehicleDAO;

@Path("/vehicle")
public class VehicleService {

	@Context
	ServletContext context;
	
	public VehicleService() {
		super();
	}

	@PostConstruct
	public void init() {

		if (context.getAttribute("vehicleDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("vehicleDAO", new VehicleDAO(contextPath));
		}
		if (context.getAttribute("userDAO") == null) {
	    	String contextPath = context.getRealPath("/");
			context.setAttribute("userDAO", new UserDAO(contextPath));
		}
		
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVehicle(@Context HttpServletRequest request, Vehicle vehicle) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		VehicleDAO vehicleDAO = (VehicleDAO) context.getAttribute("vehicleDAO");
		
		Vehicle saved = vehicleDAO.save(vehicle);
		
		return Response.ok(saved).build();
	}
	
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editVehicle(@Context HttpServletRequest request, Vehicle vehicle) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(405).entity("Nije dozvoljeno!").build();
		
		VehicleDAO vehicleDAO = (VehicleDAO) context.getAttribute("vehicleDAO");
		
		Vehicle saved = vehicleDAO.save(vehicle);
		
		return Response.ok(saved).build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteVehicle(@Context HttpServletRequest request, @PathParam("id") Long id) {
		
		User logged = (User) request.getSession().getAttribute("logged");
		if(logged==null || logged.getRole() != UserRole.ADMIN)
			return Response.status(401).entity("Nije dozvoljeno!").build();
		
		VehicleDAO vehicleDAO = (VehicleDAO) context.getAttribute("vehicleDAO");
		
		Vehicle deleted = vehicleDAO.delete(id);
		
		if(deleted == null)
			return Response.status(400).entity("Nije moguce obrisati").build();
		
		return Response.ok(deleted).build();
	}
	
}
