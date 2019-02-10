package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.UserHandler;
import java.util.ArrayList;
import model.Wallet;
import model.User;


@Path("/user")
public class UserRoutes {

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		ArrayList<User> allUsersList = UserHandler.handleGetAllUsers();
		Integer listSize = allUsersList.size();
		User[] allUsers = allUsersList.toArray(new User[listSize]);
		if (listSize > 0) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String allUsersJSON = gson.toJson(allUsers);
			return Response.status(200).entity(allUsersJSON).build();
		} else {
			String errorJSON = "{\"errorMsg\": \"It wasn't possible to find any user\"}";
			return Response.status(400).entity(errorJSON).build();
		}
	}

	@GET
	@Path("/{cpf}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByCPF(@PathParam("cpf") String cpf) {
		User par = UserHandler.handleGetUserByCPF(cpf);
		Integer id = par.getId();
		if (id > 0) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String userJSON = gson.toJson(par);
			return Response.status(200).entity(userJSON).build();
		} else {
			String errorJSON = "{\"errorMsg\": \"It wasn't possible to find user " + cpf + "\"}";
			return Response.status(400).entity(errorJSON).build();
		}
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postUser(String userJSON) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		User newUser = gson.fromJson(userJSON, User.class);
		String nome = newUser.getName();
		Integer id = UserHandler.handleUserCreation(newUser);
		if (id > 0) {
			String successJSON = "{\"successMsg\": \"It was possible to create user " + nome + "\"}";
			return Response.status(200).entity(successJSON).build();
		} else {
			String errorJSON = "{\"errorMsg\": \"It wasn't possible to create user " + nome + "\"}";
			return Response.status(400).entity(errorJSON).build();
		}
	}

	@GET
	@Path("/wallet/{cpf}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserWalletByCPF(@PathParam("cpf") String cpf) {
		Wallet car = UserHandler.handleGetUserWalletByCPF(cpf);
		Integer id = car.getId();
		if (id > 0) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String userJSON = gson.toJson(car);
			return Response.status(200).entity(userJSON).build();
		} else {
			String errorJSON = "{\"errorMsg\": \"It wasn't possible to find the wallet from user " + cpf + "\"}";
			return Response.status(400).entity(errorJSON).build();
		}
	}

}
