package gusil.mybox;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class MyboxTestUtil {
    public static final String TEST_USER1_ID = "6102c84c-39e1-4169-ad54-81cdef097f56";
    public static final String TEST_USER1_NAME = "user-for-test-1";
    public static final String TEST_USER1_JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyLWZvci10ZXN0LTEiLCJpYXQiOjE2Nzk1NTUxNTAsImV4cCI6MTY4MDE1OTk1MH0.3KdVktGTUdTPEivclLevLcbQErMc8obCgjYuZrOxcgYZh6huuSXFjFB52Rcp6vsVj0eN8TKvVIv0iCTOSkjP6A";

    public static final String TEST_USER2_ID = "b2de67c2-5937-4736-b5e1-2e7beaac9878";
    public static final String TEST_USER2_NAME = "user-for-test-2";
    public static final String TEST_USER2_JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyLWZvci10ZXN0LTIiLCJpYXQiOjE2Nzk1NTUyMDMsImV4cCI6MTY4MDE2MDAwM30.EysuQHJS8m5S95HSzq4theVUFoD_PTa7EO52oHP-RCtS6OajW8nUAoS8kZq0ZRchxh6FPhCdRM6_bc8ppvPv8w";

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
