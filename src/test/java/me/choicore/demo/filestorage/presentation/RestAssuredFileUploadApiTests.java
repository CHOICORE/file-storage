package me.choicore.demo.filestorage.presentation;


import io.restassured.RestAssured;
import jakarta.servlet.MultipartConfigElement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.InputStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAssuredFileUploadApiTests {


    public static final String OVER_SIZE_IMAGE_FILE = "/OVER_SIZE_IMAGE.jpg";
    @LocalServerPort
    private int port;

    @Autowired
    private MultipartConfigElement multipartConfigElement;

    @Test
    @DisplayName("최대 허용 크기를 초과하는 파일을 업로드하면, 413 응답을 반환한다.")
    void shouldReturnHttpResponseStatusCode413WhenUploadedFileExceedsMaxSize() throws Exception {
        try (InputStream is = assertAndGetOversizedFileInputStream()) {
            var given = RestAssured.given()
                    .port(port)
                    .multiPart("file", OVER_SIZE_IMAGE_FILE, is);

            var when = given.when()
                    .put("/v1/files");

            var then = when.then().log().all().extract();

            Assertions.assertThat(then.statusCode()).isEqualTo(413);
        }
    }

    @Test
    @DisplayName("프로퍼티에 설정한 최대 파일 사이즈를 초과하는 파일이다.")
    void t2() throws Exception {
        InputStream is = assertAndGetOversizedFileInputStream();
        Assertions.assertThat(is).isNotNull();
        long fileSize = is.readAllBytes().length;
        long maxFileSize = multipartConfigElement.getMaxFileSize();
        Assertions.assertThat(fileSize).isGreaterThan(maxFileSize);
    }

    private InputStream assertAndGetOversizedFileInputStream() {
        InputStream is = getClass().getResourceAsStream(OVER_SIZE_IMAGE_FILE);
        Assertions.assertThat(is).isNotNull();
        return is;
    }
}
