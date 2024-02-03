package Actions;

import Objects.RequestObject.RequestAccount;
import Objects.RequestObject.RequestAccountToken;
import Objects.ResponseObject.ResponseAccountSuccess;
import Objects.ResponseObject.ResponseTokenSuccess;
import Rest.RestRequestStatus;
import Service.ServiceImplementation.AccountServiceImpl;
import io.restassured.response.Response;
import org.testng.Assert;

public class AccountActions {

    public AccountServiceImpl accountService;

    public ResponseAccountSuccess createNewAccount(RequestAccount requestAccount){
        accountService = new AccountServiceImpl();
        Response response = accountService.createAccount(requestAccount); //executa aceasta actiune

        Assert.assertEquals((int) RestRequestStatus.SC_Created, response.getStatusCode());  //am specificat primita "int" si facem assert-ul

        ResponseAccountSuccess responseAccountSuccess = response.body().as(ResponseAccountSuccess.class); //face deserializarea
        Assert.assertNotNull(responseAccountSuccess.getUserID()); //facem asserturi restul 3 coloanelor in jos
        Assert.assertEquals(responseAccountSuccess.getUsername(), requestAccount.getUserName());
        Assert.assertNotNull(responseAccountSuccess.getBooks());

        return responseAccountSuccess;
    }

    public ResponseTokenSuccess generateToken(RequestAccountToken requestAccountToken){
        accountService = new AccountServiceImpl();
        Response response = accountService.generateToken(requestAccountToken);

        Assert.assertEquals(RestRequestStatus.SC_OK,response.getStatusCode());

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);
        Assert.assertNotNull(responseTokenSuccess.getToken());
        Assert.assertNotNull(responseTokenSuccess.getExpires());
        Assert.assertEquals(responseTokenSuccess.getStatus(),"Success");
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");

        return responseTokenSuccess;

    }
}