import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

public class TestClient {

    String serviceEndpoint = "http://localhost:8080/";
    String clientEndpoint = "cliente";

    @Test
    @DisplayName("When get all clients without register clients, Then the list should be empty")
    public void getAllClients(){

        String expectedResponse = "{}";

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(serviceEndpoint)
        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(expectedResponse));
    }

    @Test
    @DisplayName("When register a client, Then the client must be available on the results")
    public void registerClient(){
        String clientToRegister = "{\n" +
                "    \"id\": 7,\n" +
                "    \"idade\": 43,\n" +
                "    \"nome\": \"Rodrigo\",\n" +
                "    \"risco\": 0\n" +
                "}";

        String expectedResponse = "{\"7\":{\"nome\":\"Rodrigo\",\"idade\":43,\"id\":7,\"risco\":0}";

        given()
                .contentType(ContentType.JSON)
                .body(clientToRegister)
        .when()
                .post(serviceEndpoint + clientEndpoint)
        .then()
                .statusCode(201)
                .assertThat().body(containsString(expectedResponse));


    }
}
