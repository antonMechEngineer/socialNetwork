package soialNetworkApp.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
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
import soialNetworkApp.api.request.DialogUserShortListDto;

import java.util.ArrayList;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        DialogUserShortListDto dialogUserShortListDto = new DialogUserShortListDto();
        List<Long> users = new ArrayList<>();
        users.add(3L);
        dialogUserShortListDto.setUserIds(users);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(dialogUserShortListDto);

        mockMvc.perform(post("/api/v1/dialogs").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.count").value(2));
    }

    @Test
    void messages() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/1/messages"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void unread() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/unreaded"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.count").value(1));
    }

    @Test
    void read() throws Exception {
        mockMvc.perform(put("/api/v1/dialogs/1/read"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.count").value(1));
    }
}