package Service;

import Rest.RestRequest;
import Rest.RestRequestType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CommonAPIService {

    // Aceasta clasa contine metode pt tipuri de request-uri cu diferiti parametrii
    // ( Vom folosi polimorfismul )
    public Response post(Object body, String url){  // post fara token, prin polimorfism
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(body);

        // trebuie sa implementam logurile pt request si response

        Response response = performRequest(RestRequestType.Request_post, requestSpecification, url);
        return response;
    }

     public Response post(Object body, String url, String token){  // post cu token, prin polimorfism
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Authorization","Bearer "+ token);
        requestSpecification.body(body);

        // trebuie sa implementam logurile pt request si response

        Response response = performRequest(RestRequestType.Request_post, requestSpecification, url);
        return response;
    }

    public Response get (String url, String token){  // get cu token, polimorfism
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Authorization","Bearer "+ token);


        // trebuie sa implementam logurile pt request si response

        Response response = performRequest(RestRequestType.Request_get, requestSpecification, url);
        return response;
    }

    // Facem o instanta de RestRequest care sa apeleze metoda de performRequest
    private Response performRequest(String requestType, RequestSpecification requestSpecification,
                                    String URL){
        return new RestRequest().performRequest(requestType,requestSpecification,URL);
    }
}
