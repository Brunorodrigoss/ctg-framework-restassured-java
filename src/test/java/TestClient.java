import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestClient {

    private static final String CLIENT_SERVICE = "http://localhost:8080";
    private static final String CLIENT_RESOURCE = "/cliente";
    private static final String DELETE_ALL_CLIENTS = "/apagaTodos";
    private static final String RISK = "/risco/";
    private static final String CLIENT_LIST_EMPTY = "{}";

    @Test
    @DisplayName("When request a client's list without added any client before, Then the listt must be empty")
    public void whenRequestClientListWithoutRegisterAnyClientBefore_ThenTheListMustBeEmpty(){
        deleteAllClients();

        getAllClients()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(CLIENT_LIST_EMPTY));
    }

    @Test
    @DisplayName("When register a client, Then the client must be successfull registered")
    public void whenRegisterClient_ThenTheClienMustBeSuccessfullRegisterd() {

        Client clientToRegister = new Client("Bruno", 31, 101011);

        registerClient(clientToRegister)
                .statusCode(HttpStatus.SC_CREATED)
                .body("101011.nome", equalTo("Bruno"))
                .body("101011.idade", equalTo(31))
                .body("101011.id", equalTo(101011));
    }

    @Test
    @DisplayName("When update a client, Then he be able to update successfully")
    public void whenUpdateClient_ThenHeBeAbleToUpdateSuccessfully() {

        Client clientToRegister = new Client("Sherlock", 50, 202022);

        registerClient(clientToRegister);

        clientToRegister.setNome("Holmes, Sherlock");
        clientToRegister.setIdade(55);

        updateClient(clientToRegister)
                .statusCode(HttpStatus.SC_OK)
                .body("202022.nome", equalTo("Holmes, Sherlock"))
                .body("202022.idade", equalTo(55))
                .body("202022.id",equalTo(202022));
    }

    @Test
    @DisplayName("When you delete an existing client, Then client will be successfully deleted")
    public void whenYouDeleteAnExistingClient_ThenClientWillBeSuccessfullyDeleted() {
       Client client = new Client("Mr Robot", 28, 777);

       registerClient(client);

       deleteClient(client)
               .statusCode(HttpStatus.SC_OK)
               .assertThat().body(not(contains("Mr Robot")));

    }

    @Test
    @DisplayName("When ask a client risk using valid credentials, Then it must be returned success")
    public void whenAskClientRiskUsingValidCredentials_ThenMustBeReturnedSuccess() {
        Client client = new Client("Walter White", 32, 190190);

        int expectedRisk = - 50;

        registerClient(client);

        given()
                .auth()
                    .basic("aluno", "senha")
        .when()
                .get(CLIENT_SERVICE + RISK + client.getId())
        .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("risco", equalTo(expectedRisk));
    }

    private ValidatableResponse getAllClients() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(CLIENT_SERVICE)
                .then();
    }

    private ValidatableResponse registerClient(Client clientToRegister) {
        return given()
                .contentType(ContentType.JSON)
                .body(clientToRegister)
                .when()
                .post(CLIENT_SERVICE + CLIENT_RESOURCE)
                .then();
    }

    private ValidatableResponse updateClient(Client clientToUpdate) {
        return given()
                .contentType(ContentType.JSON)
                .body(clientToUpdate)
                .when()
                .put(CLIENT_SERVICE + CLIENT_RESOURCE)
                .then();
    }

    private ValidatableResponse deleteClient(Client clientToDelete) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(CLIENT_SERVICE + CLIENT_RESOURCE + "/" + clientToDelete.getId())
                .then();
    }

    @AfterEach
    public void deleteAllClients() {
        when()
                .delete( CLIENT_SERVICE + CLIENT_RESOURCE + DELETE_ALL_CLIENTS)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(CLIENT_LIST_EMPTY));
    }
}
