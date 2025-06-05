package fr.middlewaresolutions.demo;

import fr.middlewaresolutions.demo.services.ClientsService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TestClients {

  @Inject ClientsService clientsService;

  @Test
  public void testListWithHTTP() {
    given()
        .when().get("/clients")
        .then()
        .statusCode(200)
        .body(is("[]"));
  }

  @Test
  public void testEmptyListWithService() {
    try(Response response = clientsService.listClients(null, null)) {
      List<?> list = response.readEntity(List.class);
      Assertions.assertEquals(0, list.size());
    }
  }

}
