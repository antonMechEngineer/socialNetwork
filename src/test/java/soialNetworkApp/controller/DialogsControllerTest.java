package soialNetworkApp.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@Sql("/test-data.sql")
@WithUserDetails("rhoncus.nullam@yahoo.edu")
class DialogsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dialogs() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void dialogsStart() throws Exception {
        mockMvc.perform(post("/api/v1/dialogs"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void messages() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/{dialogId}/messages"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void unread() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/unreaded"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void read() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/{dialogId}/read"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}