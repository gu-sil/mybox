package gusil.mybox;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class MyboxTestUtil {
    public static final String TEST_USER1_ID = "da54fffc-2afb-4dea-8899-a4ed6bf1ee84";
    public static final String TEST_USER1_NAME = "user-for-unit-test-1";
    public static final String TEST_USER1_JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyLWZvci11bml0LXRlc3QtMSIsImlhdCI6MTY3OTY3MjkxMywiZXhwIjoxNjgwMjc3NzEzfQ.0vVuqZCqAYpCJlPgPbTwyjXdSvWkkh40yoaAMLP0HwDEQTLfRABy6LqWKmulLt01qPc8ADozv1Euu4HDftvxjg";

    public static final String TEST_USER2_ID = "e9181f51-22c6-49d8-86d4-a38f8eb20465";
    public static final String TEST_USER2_NAME = "user-for-unit-test-2";
    public static final String TEST_USER2_JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyLWZvci11bml0LXRlc3QtMiIsImlhdCI6MTY3OTY3Mjk4MSwiZXhwIjoxNjgwMjc3NzgxfQ.Gqv7UwY2XDtaowXDEcX3NRr9p8480HOpW3_QvluLTPr8ZNGoncu1V9wUk1aFJOrf3ig5BYH9QLW64exI0rWnuQ";

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static ExtractableResponse<Response> createDirectory(
            String directoryName, String directoryOwnerId, String directoryParent, String ownerJwt) {

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("directoryName", directoryName);
        bodyMap.put("directoryOwner", directoryOwnerId);
        bodyMap.put("directoryParent", directoryParent);

        return RestAssured.given()
                .header(AUTH_HEADER_KEY, ownerJwt)
                .body(bodyMap)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/api/v1/users/" + directoryOwnerId+ "/directories")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> readDirectoryChildren(String dirId, String ownerId, String ownerJwt) {
        return RestAssured.given()
                .header(AUTH_HEADER_KEY, ownerJwt)
                .log().all()
                .when()
                .get("/api/v1/users/" + ownerId + "/directories/" + dirId + "/items")
                .then().log().all()
                .extract();
    }

    public static String getUserCurrentUsage(String userId, String userJwt) {
        return RestAssured.given()
                .header(AUTH_HEADER_KEY, userJwt)
                .log().all()
                .when()
                .get("/api/v1/users/" + userId)
                .then().log().all()
                .extract().body().jsonPath().get("currentUsage").toString();
    }

    public static ExtractableResponse<Response> uploadFile(
            int fileSize, String userId, String fileName, String dirId, String ownerJwt) {

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("filePart", RandomStringUtils.random(fileSize, false, true));
        bodyMap.put("userId", userId);
        bodyMap.put("fileName", fileName);

        return RestAssured.given()
                .header(AUTH_HEADER_KEY, ownerJwt)
                .body(bodyMap)
                .contentType(ContentType.MULTIPART)
                .log().all()
                .when()
                .post("/api/v1/directories/" + dirId + "/files")
                .then().log().all()
                .extract();
    }

}
