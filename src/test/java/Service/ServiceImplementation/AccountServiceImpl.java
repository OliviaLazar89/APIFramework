package Service.ServiceImplementation;

import EndPoints.AccountEndPoints;
import RequestObject.RequestAccount;
import RequestObject.RequestAccountToken;
import Service.APIService.AccountAPIService;
import Service.InterfaceService.AccountServiceInterface;
import io.restassured.response.Response;

public class AccountServiceImpl implements AccountServiceInterface {

    public AccountAPIService accountAPIService;
    @Override
    public Response createAccount(RequestAccount requestAccount) {
        accountAPIService = new AccountAPIService();
        return accountAPIService.post(requestAccount, AccountEndPoints.ACCOUNT_CREATE);
    }

    @Override
    public Response generateToken(RequestAccountToken requestAccountToken) {
    accountAPIService = new AccountAPIService();
    return accountAPIService.post(requestAccountToken, AccountEndPoints.ACCOUNT_TOKEN);
    }

    @Override
    public Response getSpecificAccount(String userID, String token) {
        accountAPIService = new AccountAPIService();
        String finalEndPoint = AccountEndPoints.ACCOUNT_USERSPECIFIC.replace("{userID}",userID);  // //face replace la endpoint-ul din AccountEndPoints
        return accountAPIService.get(finalEndPoint, token);
    }
}
