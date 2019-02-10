package routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import handlers.DepositHandler;
import model.Transacao;


@Path("/deposit")
public class Deposit {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response depositContribuicaoNormal(String transacaoJSON) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Transacao tran = gson.fromJson(transacaoJSON, Transacao.class);
		Integer value = tran.getQtdParcelas()*tran.getValorParcela();
		String cpfParticipante = tran.getCpfParticipante();
		Integer depositId = DepositHandler.handleDeposit(tran);
		if (depositId > 0) {
			String successJSON = String.format("{\"successMsg\": \"It was possible to deposit R$%d to %s\"}", value, cpfParticipante);
			return Response.status(200).entity(successJSON).build();
		} else {
			String errorJSON =  String.format("{\"errorMsg\": \"It wasn't possible to deposit R$%d to %s\"}", value, cpfParticipante);
			return Response.status(400).entity(errorJSON).build();
		}
	}
}
