package me.choicore.demo.filestorage.presentation;

import me.choicore.demo.filestorage.common.FileManager;
import me.choicore.demo.filestorage.common.FileProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@WebMvcTest
class MockMvcFileUploadApiTests {

    private static final String TEST_IMAGE_NAME = "image.jpg";

    @MockBean
    private FileProperties fileProperties;

    @MockBean
    private FileManager fileManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("파일 업로드에 성공하면, 고유 식별자와 메타정보를 응답한다.")
    void t1() throws Exception {
        MockMultipartFile file = getMockMultipartFile();

        assertMockBean(file);

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/v1/files")
                        .file(file))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.sid").isNotEmpty(),
                        MockMvcResultMatchers.jsonPath("$.filename").value(TEST_IMAGE_NAME),
                        MockMvcResultMatchers.jsonPath("$.size").value(file.getSize())
                )
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    private void assertMockBean(final MockMultipartFile file) throws IOException {
        Mockito.when(fileProperties.baseDir()).thenReturn("/uploads");
        Mockito.when(fileManager.upload(file)).thenReturn(UUID.randomUUID().toString());

        Assertions.assertThat(fileProperties.baseDir()).isNotEmpty().isEqualTo("/uploads");
        Assertions.assertThat(fileManager.upload(file)).isNotEmpty();
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME);
        Assertions.assertThat(is).isNotNull();
        return new MockMultipartFile("file", "image.jpg", "image/jpeg", is);
    }
}