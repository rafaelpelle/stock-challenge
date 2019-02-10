package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.WithdrawHandler;
import model.Transaction;


@Path("/withdraw")
public class WithdrawRoutes {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeAWithdraw(String transacaoJSON) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Transaction tran = gson.fromJson(transacaoJSON, Transaction.class);
		Integer value = tran.getNumberOfInstallments()*tran.getInstallmentValue();
		String userCpf = tran.getUserCpf();
		Integer withdrawId = WithdrawHandler.handleWithdraw(tran);
		if (withdrawId > 0) {
			String successJSON = String.format("{\"successMsg\": \"It was possible to withdraw R$%d from %s\"}", value, userCpf);
			return Response.status(200).entity(successJSON).build();
		} else {
			String errorJSON =  String.format("{\"errorMsg\": \"It wasn't possible to withdraw R$%d from %s\"}", value, userCpf);
			return Response.status(400).entity(errorJSON).build();
		}
	}
}
