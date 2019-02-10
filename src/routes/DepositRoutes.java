package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.DepositHandler;
import model.Transaction;


@Path("/deposit")
public class DepositRoutes {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeADeposit(String transacaoJSON) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Transaction tran = gson.fromJson(transacaoJSON, Transaction.class);
		Integer value = tran.getNumberOfInstallments()*tran.getInstallmentValue();
		String userCpf = tran.getUserCpf();
		Integer depositId = DepositHandler.handleDeposit(tran);
		if (depositId > 0) {
			String successJSON = String.format("{\"successMsg\": \"It was possible to deposit R$%d to %s\"}", value, userCpf);
			return Response.status(200).entity(successJSON).build();
		} else {
			String errorJSON =  String.format("{\"errorMsg\": \"It wasn't possible to deposit R$%d to %s\"}", value, userCpf);
			return Response.status(400).entity(errorJSON).build();
		}
	}
}
