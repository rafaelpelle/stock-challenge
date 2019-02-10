package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.UserHandler;
import model.Carteira;
import model.Participante;


@Path("/user")
public class User {

	@GET
	@Path("/{cpf}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByCPF(@PathParam("cpf") String cpf) {
		Participante par = UserHandler.handleGetUserByCPF(cpf);
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
		Participante newUser = gson.fromJson(userJSON, Participante.class);
		String nome = newUser.getNome();
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
		Carteira car = UserHandler.handleGetUserWalletByCPF(cpf);
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
