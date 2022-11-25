package main.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.PostRequest;
import main.model.entities.Post;
import main.repository.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@Sql("/test-data.sql")
@AutoConfigureMockMvc
@WithUserDetails("rhoncus.nullam@yahoo.edu")
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFeeds() throws Exception {
        mockMvc.perform(get("/api/v1/feeds"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getPost() throws Exception {
        mockMvc.perform(get("/api/v1/post/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void updatePost() throws Exception {
        PostRequest request = new PostRequest();
        request.setTitle("newTitle");
        request.setPostText("newText");
        request.setTags(new ArrayList<>());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/post/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.title").value("newTitle"))
                .andExpect(jsonPath("$.data.post_text").value("newText"));
    }

    @Test
    void deleteAndRecoverPost() throws Exception {
        mockMvc.perform(delete("/api/v1/post/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.type").value("DELETED"));
    }

    @Test
    void recoverPost() throws Exception {
        mockMvc.perform(delete("/api/v1/post/1")).andDo(print());
        mockMvc.perform(put("/api/v1/post/1/recover"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.type").value("POSTED"));
    }
}