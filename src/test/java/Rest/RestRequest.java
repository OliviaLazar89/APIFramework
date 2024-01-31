package Rest;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestRequest {

    // Facem o metoda care sa execute un request la un endpoint
    public Response performRequest(String requestType,
                                   RequestSpecification requestSpecification, String URL){
        switch (requestType ){
            case RestRequestType.Request_delete:
                return prepare(requestSpecification).delete(URL); // pt orice fel de request de tip delete
            case RestRequestType.Request_get:
                return prepare(requestSpecification).get(URL);
            case RestRequestType.Request_post:
                return prepare(requestSpecification).post(URL);
            case RestRequestType.Request_put:
                return prepare(requestSpecification).put(URL);
        }
        return null;
    }


    // Trebuie sa configurez setarile pt client
    public RequestSpecification prepare(RequestSpecification requestSpecification){
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        return requestSpecification;
    }



}
