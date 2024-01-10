package me.choicore.demo.filestorage.presentation;

import jakarta.servlet.MultipartConfigElement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TestMultipartConfigurer.class)
class TestMultipartConfigurerTests {

    private static final String TEST_IMAGE_NAME = "image.jpg";

    @Autowired
    private MultipartConfigElement multipartConfigElement;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void printWebApplicationContext() {
        Assertions.assertThat(webApplicationContext).isNotNull();
        for (String beanDefinitionName : webApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }

    @Test
    void printMultipartConfigElement() {
        Assertions.assertThat(multipartConfigElement).isNotNull();
        System.out.println(multipartConfigElement.getLocation());
        System.out.println(multipartConfigElement.getMaxFileSize());
        System.out.println(multipartConfigElement.getMaxRequestSize());
        System.out.println(multipartConfigElement.getFileSizeThreshold());
    }

    @Test
    @DisplayName("파일 업로드에 성공하면, 고유 식별자와 메타정보를 응답한다.")
    void t1() throws Exception {
        MockMultipartFile file = getMockMultipartFile();
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

    @Test
    @DisplayName("프로퍼티에 설정한 최대 파일 사이즈를 초과하는 파일이다.")
    void t2() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME);
        Assertions.assertThat(is).isNotNull();
        long fileSize = is.readAllBytes().length;
        long maxFileSize = multipartConfigElement.getMaxFileSize();
        Assertions.assertThat(fileSize).isGreaterThan(maxFileSize);
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_IMAGE_NAME);
        Assertions.assertThat(is).isNotNull();
        return new MockMultipartFile("file", "image.jpg", "image/jpeg", is);
    }
}
