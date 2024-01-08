package me.choicore.demo.filestorage.presentation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FileUploadApiTests {

    private static final String TEST_IMAGE_NAME = "image.jpg";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("파일 업로드에 성공하면, 고유 식별자와 메타정보를 응답한다.")
    void t1() throws Exception {

        MockMultipartFile file = getMockMultipartFile();

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/v1/files")
                        .file(file))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                )
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sid").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.filename").value(TEST_IMAGE_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(file.getSize()));
    }

    @Test
    @DisplayName("")
    void t2() {

    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME);
        Assertions.assertThat(is).isNotNull();
        return new MockMultipartFile("file", "image.jpg", "image/jpeg", is);
    }
}