package Tests;

import Actions.AccountActions;
import Hooks.Hooks;
import Objects.RequestObject.RequestAccount;
import Objects.RequestObject.RequestAccountToken;
import Objects.ResponseObject.ResponseAccountAuthSuccess;
import Objects.ResponseObject.ResponseAccountSuccess;
import Objects.ResponseObject.ResponseTokenSuccess;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserV2Test extends Hooks {

    public String userID;
    public String username;
    public String password;
    public String token;
    public AccountActions accountActions;



    @Test
    public void testMethod(){  //facem intregul flow in acest test pentru a pastra logica de creare user, autentificare, token
        //Adica facem aici toate REQUESTURILE DE BACKEND

        System.out.println("Step 1 - create user");
        createUser();

        System.out.println("Step 2 - generate token");
        generateToken();
//
//        System.out.println("Step 3 - obtain new user");
//        interractNewUser();

    }

    public void createUser(){  //PASUL 1 CREEAM USERUL CARE SA NE OFERE ID-UL

        accountActions = new AccountActions();
        username = "Olivia" + System.currentTimeMillis(); //va genera valoare unica
        password = "Password!??@#1234";

        RequestAccount requestAccount = new RequestAccount(username,password );
        ResponseAccountSuccess responseAccountSuccess = accountActions.createNewAccount(requestAccount);

        userID = responseAccountSuccess.getUserID();

    }

    public void generateToken(){  //PASUL 2 - FACEM POSTUL CARE NE DA UN TOKEN

        accountActions = new AccountActions();

        RequestAccountToken requestAccountToken=new RequestAccountToken(username,password);
        ResponseTokenSuccess responseTokenSuccess=accountActions.generateToken(requestAccountToken);
        token=responseTokenSuccess.getToken();

    }

    public void interractNewUser(){

        RequestSpecification requestSpecification = RestAssured.given(); //configuram clientul cu anumite specificatii
        requestSpecification.baseUri("https://demoqa.com"); //specficam url-ul de baza pe care vrem sa il configuram
        requestSpecification.contentType("application/json"); //specificam ca e contentul de tip Jason
        requestSpecification.header("Authorization", "Bearer " + token); //autorizare care foloseste token

        Response response = requestSpecification.get("/Account/v1/User/" + userID); //compunere de endpoint

        Assert.assertEquals(response.getStatusCode(), 200);

        ResponseAccountAuthSuccess responseAccountAuthSuccess = response.body().as(ResponseAccountAuthSuccess.class);
        Assert.assertNotNull(responseAccountAuthSuccess.getUserId());  //verificam ca exista o valoare pt id, cat nu e nul
        Assert.assertEquals(responseAccountAuthSuccess.getUsername(), username); //verif ca username are valoarea din request
        Assert.assertNotNull(responseAccountAuthSuccess.getBooks());  //verificam ca books sa contina cel putin "["

    }
}
