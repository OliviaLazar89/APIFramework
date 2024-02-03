package Tests;

import Actions.AccountActions;
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

public class CreateUserTest {

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

//        System.out.println("Step 2 - generate token");
//        generateToken();
//
//        System.out.println("Step 3 - obtain new user");
//        interractNewUser();

    }

    public void createUser(){  //PASUL 1 CREEAM USERUL CARE SA NE OFERE ID-UL

//        RequestSpecification requestSpecification = RestAssured.given(); //configuram clientul cu anumite specificatii
//        requestSpecification.baseUri("https://demoqa.com"); //specficam url-ul de baza pe care vrem sa il configuram
//        requestSpecification.contentType("application/json"); //specificam ca e contentul de tip Jason

        accountActions = new AccountActions();
        username = "Olivia" + System.currentTimeMillis(); //va genera valoare unica
        password = "Password!??@#1234";
        RequestAccount requestAccount = new RequestAccount(username,password );
        ResponseAccountSuccess responseAccountSuccess = accountActions.createNewAccount(requestAccount);

        userID = responseAccountSuccess.getUserID(); //am salvat userID-ul pentru a-l folosi la urmatorul pas


        //Configuram request-ul

//        JSONObject requestBody = new JSONObject();  //construim un body de tipul Jason, functioneaza ca un fel de hashmap (cheie - valoare)
//        requestBody.put("userName",userName);  //stabilim cheia-valoarea
//        requestBody.put("password", "Password!??@#1234");  //stabilim cheia-valoarea

        //Am facut SEREALIZARE, am transformat body-ul sub forma unui obiect facut de noi
//        requestSpecification.body(requestAccount);   //atasam body-ul pe constructia clientului (cea de sus)


        //Accesam response-ul

//        Response response = requestSpecification.post("/Account/v1/User");  // accesam raspunsul trimitand un request de tip POST
////        System.out.println(response.body());
//        ResponseBody body = response.getBody();
//        body.prettyPrint(); //folosim in loc de system out

        //Validam statusul requestului

//        Assert.assertEquals(response.getStatusCode(), 201); //principala validare
//
//        //Validam response body-ul requestului
//
//        ResponseAccountSuccess responseAccountSuccess = response.body().as(ResponseAccountSuccess.class);
//        Assert.assertNotNull(responseAccountSuccess.getUserID());  //verificam ca exista o valoare pt ID, ca nu e nul
//        Assert.assertEquals(responseAccountSuccess.getUsername(), username); //verif ca username are valoarea din request
//        Assert.assertNotNull(responseAccountSuccess.getBooks());  //verificam ca books sa contina cel putin "["


    }

    //facem un request POST care ne da token-ul - Acum facem autentificarea

    public void generateToken(){  //PASUL 2 - FACEM POSTUL CARE NE DA UN TOKEN

        RequestSpecification requestSpecification = RestAssured.given(); //configuram clientul cu anumite specificatii
        requestSpecification.baseUri("https://demoqa.com"); //specficam url-ul de baza pe care vrem sa il configuram
        requestSpecification.contentType("application/json"); //specificam ca e contentul de tip Jason

        RequestAccountToken requestAccountToken = new RequestAccountToken(username,password);

        requestSpecification.body(requestAccountToken);   //atasam body-ul pe constructia clientului (cea de sus)

        //Accesam response-ul

        Response response = requestSpecification.post("/Account/v1/GenerateToken");  // accesam raspunsul trimitand un request de tip POST
//        System.out.println(response.body());
        ResponseBody body = response.getBody();
        body.prettyPrint(); //folosim in loc de system out

        Assert.assertEquals(response.getStatusCode(), 200);  //validarea de status code

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);
        Assert.assertNotNull(responseTokenSuccess.getToken());  //verificam ca exista o valoare , ca nu e nul
        Assert.assertNotNull(responseTokenSuccess.getExpires());
        Assert.assertEquals(responseTokenSuccess.getStatus(),"Success"); //verif ca username are valoarea din request
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");  //verificam ca books sa contina cel putin "["

        token = responseTokenSuccess.getToken(); //am extras/salvat tokenul pentru a-l folosi pentru urmatorul pas

    }

    //facem un GET pentru userul creat/generat

    public void interractNewUser(){  //PASUL 3 FACEM AUTORIZAREA

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

