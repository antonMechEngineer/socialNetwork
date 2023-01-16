package soialNetworkApp.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
class DialogsControllerTest {

    private final MockMvc mockMvc;

    @Test
    void dialogs() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs"))
                .andDo(print());
    }

    @Test
    void dialogsStart() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs"))
                .andDo(print());
    }

    @Test
    void messages() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/{dialogId}/messages"))
                .andDo(print());
    }

    @Test
    void unread() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/unreaded"))
                .andDo(print());
    }

    @Test
    void read() throws Exception {
        mockMvc.perform(get("/api/v1/dialogs/{dialogId}/read"))
                .andDo(print());
    }
}