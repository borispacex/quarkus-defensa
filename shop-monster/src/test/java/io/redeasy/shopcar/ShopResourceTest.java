package io.redeasy.shopcar;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ShopResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/shops")
          .then()
             .statusCode(200);
    }

}