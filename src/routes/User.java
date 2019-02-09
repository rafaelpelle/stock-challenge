package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.UserHandler;
import modelo.Participante;


@Path("/user")
public class User {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHelloWorld(String participante) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Participante novoParticipante = gson.fromJson(participante, Participante.class);
		String nome = novoParticipante.getNome();
		Integer id = UserHandler.handleParticipanteCreation(novoParticipante);
		String successMsg = "{\"successMsg\": \"It was possible to create user " + nome + "\"}";
		String errorMsg = "{\"errorMsg\": \"It wasn't possible to create user " + nome + "\"}";
		if (id > 0) {
			return Response.status(200).entity(successMsg).build();
		} else {
			return Response.status(400).entity(errorMsg).build();
		}
	}

}
