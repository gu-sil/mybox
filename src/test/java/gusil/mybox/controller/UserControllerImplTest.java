package gusil.mybox.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static gusil.mybox.MyboxTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerImplTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getExistUser_shouldGet200AndUserInfo() {
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .header(AUTH_HEADER_KEY, TEST_USER1_JWT)
                        .log().all()
                        .when()
                        .get("/api/v1/users/" + TEST_USER1_ID)
                        .then().log().all()
                        .extract();

        // assert
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().get("userName").toString()).isEqualTo(TEST_USER1_NAME);
    }

    @Test
    void getNotExistUser_shouldGet404() {
        String notExistUserId = "shouldNotExistId";
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .header(AUTH_HEADER_KEY, TEST_USER1_JWT)
                        .log().all()
                        .when()
                        .get("/api/v1/users/" + notExistUserId)
                        .then().log().all()
                        .extract();

        // assert
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}