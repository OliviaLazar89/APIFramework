package Tests;

import RequestObject.RequestAccount;
import ResponseObject.ResponseAccountFailed;
import ResponseObject.ResponseAccountSuccess;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserInvalidTest {

    public String userID;
    public String username;
    public String password;


    @Test
    public void testMethod() {

        System.out.println("Step 1 : Create User");
        createUser();


    }

    public void createUser() {

        // Definim caracteristicile clientului

        RequestSpecification requestSpecification = RestAssured.given(); // configuram clientul cu anumite specificatii
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        // Configuram request-ul

        username = "Olivia123";  // lasam asa ca de fiecare data cand rulam cu acelasi id, sa fie test failed cu eroarea respectiva
        password = "Olivia123LAZ.=#$lk";

        RequestAccount requestAccount = new RequestAccount(username, password);
        requestSpecification.body(requestAccount);

        // Accesam response-ul

        Response response = requestSpecification.post("/Account/v1/User");
        ResponseBody body = response.getBody();
        body.prettyPrint();  // preetyprint se comporta ca un system out

        // Validam statusul request-ului

        Assert.assertEquals(response.getStatusCode(), 406); // principala validare de scenariu negativ

        // Validam response body

        ResponseAccountFailed responseAccountFailed = response.body().as(ResponseAccountFailed.class);


        Assert.assertNotNull(responseAccountFailed.getCode());
        Assert.assertEquals(responseAccountFailed.getMessage(),"User exists!" );

    }
}
