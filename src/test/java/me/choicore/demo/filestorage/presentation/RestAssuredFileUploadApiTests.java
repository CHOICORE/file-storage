package me.choicore.demo.filestorage.presentation;


import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.InputStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAssuredFileUploadApiTests {

    @LocalServerPort
    private int port;

    @Test
    void t1() {
        var given = RestAssured.given()
                .port(port)
                .multiPart("file", "image.jpg", assertGetFileInputStream());

        var when = given.when()
                .put("/v1/files");

        var then = when.then().log().all().extract();

        Assertions.assertThat(then.statusCode()).isEqualTo(413);
    }

    private InputStream assertGetFileInputStream() {
        return getClass().getResourceAsStream("/FILE_MAX_SIZE_OVER_FILE.jpg");
    }
}
