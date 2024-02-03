package Objects.ResponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseAccountSuccess {

    @JsonProperty("userID")
    private String userID;      // asa se face maparea primei variabile pe care respone ul o are
    @JsonProperty("username")
    private String username;
    @JsonProperty("books")
    private List<BookObject> books;

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public List<BookObject> getBooks() {
        return books;
    }
}
