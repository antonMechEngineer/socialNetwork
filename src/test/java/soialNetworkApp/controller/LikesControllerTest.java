package soialNetworkApp.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import soialNetworkApp.api.request.LikeRq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;

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
class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getLikesList() throws Exception {
        mockMvc.perform(get("/api/v1/likes")
                        .param("item_id", "1")
                        .param("type", "Post"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.likes").exists())
                .andExpect(jsonPath("$.data.users").exists());
    }

    @Test
    void putLike() throws Exception {
        LikeRq request = new LikeRq();
        request.setType("Post");
        request.setItemId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/likes").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.likes").value(1))
                .andExpect(jsonPath("$.data.users").value(new ArrayList<>(List.of(1))));
    }

    @Test
    void deleteLike() throws Exception {
        LikeRq request = new LikeRq();
        request.setType("Post");
        request.setItemId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print());
        mockMvc.perform(delete("/api/v1/likes")
                        .param("item_id", "1")
                        .param("type", "Post"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.likes").value(0))
                .andExpect(jsonPath("$.data.users").value(new ArrayList<>()));
    }
}