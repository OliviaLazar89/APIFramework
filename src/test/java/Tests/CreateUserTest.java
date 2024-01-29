package Tests;

import RequestObject.RequestAccount;
import RequestObject.RequestAccountToken;
import ResponseObject.ResponseAccountAuthSuccess;
import ResponseObject.ResponseAccountSuccess;
import ResponseObject.ResponseTokenSuccess;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserTest {

    public String userID;
    public String username;
    public String password;
    public String token;

    @Test
    public void testMethod() {

        System.out.println("Step 1 : Create User");
        createUser();
        System.out.println("Step 1 : Generate Token");
        generateToken();
        System.out.println("Step 3 : Obtain new user");
        interractNewUser();

    }

    public void createUser() {

        // Definim caracteristicile clientului

        RequestSpecification requestSpecification = RestAssured.given(); // configuram clientul cu anumite specificatii
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        // Configuram request-ul

        username = "Olivia" + System.currentTimeMillis();
        password = "Olivia123LAZ.=#$";

//        JSONObject requestBody = new JSONObject(); // e un obiect care se comporta ca un hash-map, cere cheie-valoare
//        requestBody.put("userName",username);
//        requestBody.put("password","Olivia123LAZ.=#$");

        RequestAccount requestAccount = new RequestAccount(username, password);
        requestSpecification.body(requestAccount);

        // Accesam response-ul

        Response response = requestSpecification.post("/Account/v1/User");
        ResponseBody body = response.getBody();
        body.prettyPrint();  // preetyprint se comporta ca un system out

        // Validam statusul request-ului

        //System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201); // principala validare

        // Validam response body

        ResponseAccountSuccess responseAccountSuccess = response.body().as(ResponseAccountSuccess.class);
//        System.out.println(responseAccountSuccess.getUserID());

        Assert.assertNotNull(responseAccountSuccess.getUserID()); // verificam ca exista o valoare pt field ul id, care e dinamic, se tot schimba
        Assert.assertEquals(responseAccountSuccess.getUsername(), username);  // verificam ca username are valoarea din request
        Assert.assertNotNull(responseAccountSuccess.getBooks());

        userID = responseAccountSuccess.getUserID();
    }

    //Facem un request care ne genereaza un token - Autentificare si Autorizare

    public void generateToken() {

        RequestSpecification requestSpecification = RestAssured.given(); // configuram clientul cu anumite specificatii
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        RequestAccountToken requestAccountToken = new RequestAccountToken(username, password);

        requestSpecification.body(requestAccountToken);

        // Accesam response-ul

        Response response = requestSpecification.post("/Account/v1/GenerateToken");
        ResponseBody body = response.getBody();
        body.prettyPrint();  // preetyprint se comporta ca un system out

        Assert.assertEquals(response.getStatusCode(), 200);

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);

        Assert.assertNotNull(responseTokenSuccess.getToken());
        Assert.assertNotNull(responseTokenSuccess.getExpires());
        Assert.assertEquals(responseTokenSuccess.getStatus(), "Success");
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");

        token = responseTokenSuccess.getToken();
    }


    // Facem un get pt userul creat

    public void interractNewUser() {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        requestSpecification.header("Authorization","Bearer "+ token); // autorizarea care foloseste token

        Response response = requestSpecification.get("/Account/v1/User/"+ userID); // compunere de endpoint din url+userID
        Assert.assertEquals(response.getStatusCode(), 200);

        ResponseAccountAuthSuccess responseAccountAuthSuccess = response.body().as(ResponseAccountAuthSuccess.class);

        Assert.assertNotNull(responseAccountAuthSuccess.getUserId()); // verificam ca exista o valoare pt field
        Assert.assertEquals(responseAccountAuthSuccess.getUsername(), username); // verificam ca username are valoarea din request
        Assert.assertNotNull(responseAccountAuthSuccess.getBooks());
    }
}

