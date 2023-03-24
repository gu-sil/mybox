package gusil.mybox.controller;

import groovy.lang.Tuple2;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static gusil.mybox.MyboxTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DirectoryControllerImplTest {
    @LocalServerPort
    int port;

    private final List<Tuple2<String, String>> OwnerIdAndDirIdToDelete = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createDirectory_withValidParam_shouldGet200AndDirectoryInfo() {
        String dirName = "test-dir-1";
        ExtractableResponse<Response> response =
               createDirectory(dirName, TEST_USER1_ID, "root", TEST_USER1_JWT);

        // assert
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().get("directoryName").toString()).isEqualTo("test-dir-1");
        assertThat(getUserCurrentUsage(TEST_USER1_ID, TEST_USER1_JWT)).isEqualTo("0");

        OwnerIdAndDirIdToDelete.add(
                new Tuple2<>(TEST_USER1_ID, response.body().jsonPath().get("directoryId").toString()));
    }



    @Test
    void createDirectory_withDuplicatedName_shouldGet409() {
        // given
        String dirName = "test-dir-1";
        ExtractableResponse<Response> responseBefore = createDirectory(dirName, TEST_USER1_ID, "root", TEST_USER1_JWT);

        // when
        ExtractableResponse<Response> response =
                createDirectory(dirName, TEST_USER1_ID, "root", TEST_USER1_JWT);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());

        OwnerIdAndDirIdToDelete.add(
                new Tuple2<>(TEST_USER1_ID, responseBefore.body().jsonPath().get("directoryId").toString()));
    }

    @Test
    void readDirectoryChildren_withExistIdAndValidUser_shouldGet200AndChildrenList() {
        // given
        String dirNameParent = "test-dir-1";
        String createdDirParentId = createDirectory(dirNameParent, TEST_USER1_ID, "root", TEST_USER1_JWT)
                .body()
                .jsonPath()
                .get("directoryId")
                .toString();
        String dirNameChild = "test-dir-2";
        String createdDirChildId = createDirectory(dirNameChild, TEST_USER1_ID, createdDirParentId, TEST_USER1_JWT)
                .body()
                .jsonPath()
                .get("directoryId")
                .toString();

        // when
        ExtractableResponse<Response> response = readDirectoryChildren(createdDirParentId, TEST_USER1_ID, TEST_USER1_JWT);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().get("items[0].itemId").toString())
                .isEqualTo(createdDirChildId);

        OwnerIdAndDirIdToDelete.add(new Tuple2<>(TEST_USER1_ID, createdDirChildId));
        OwnerIdAndDirIdToDelete.add(new Tuple2<>(TEST_USER1_ID, createdDirParentId));
    }

    @Test
    void readDirectoryChildren_withNotExistIdAndValidUser_shouldGet404() {
        // when
        ExtractableResponse<Response> response = readDirectoryChildren("not-exist-id", TEST_USER1_ID, TEST_USER1_JWT);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void readDirectoryChildren_withExistIdAndInvalidUser_shouldGet401() {
        // given
        String dirName = "test-dir-1";
        String createdDirId = createDirectory(dirName, TEST_USER1_ID, "root", TEST_USER1_JWT)
                .body()
                .jsonPath()
                .get("directoryId")
                .toString();

        // when
        ExtractableResponse<Response> response = readDirectoryChildren(createdDirId, TEST_USER1_ID, TEST_USER2_JWT);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        OwnerIdAndDirIdToDelete.add(new Tuple2<>(TEST_USER1_ID, createdDirId));
    }

    @AfterEach
    void tearDown() {
        OwnerIdAndDirIdToDelete.forEach(ownerIdAndDirId -> {
            ExtractableResponse<Response> response = RestAssured.given()
                    .header(AUTH_HEADER_KEY, TEST_USER1_JWT)
                    .log().all()
                    .when()
                    .delete("/api/v1/users/" + ownerIdAndDirId.getV1() + "/directories/" + ownerIdAndDirId.getV2())
                    .then().log().all()
                    .extract();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });
        OwnerIdAndDirIdToDelete.clear();
    }
}