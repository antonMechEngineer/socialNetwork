package main.controller;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@Sql("/test-dataANN.sql")
@AutoConfigureMockMvc
@WithUserDetails("rhoncus.nullam@yahoo.edu")
class FriendsControllerTest {

    @Autowired
    FriendshipsRepository friendshipsRepository;

    @Autowired
    FriendshipStatusesRepository friendshipStatusesRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendFriendshipRequest() throws Exception {
        Integer expectedNumberFriendshipsAfterSending = 8;
        mockMvc.perform(post("/api/v1/friends/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertEquals(expectedNumberFriendshipsAfterSending, friendshipsRepository.findAll().size());
    }

    @Test
    void addFriend() throws Exception {
        mockMvc.perform(post("/api/v1/friends/request/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(2));
    }

    @Test
    void getPotentialFriends() throws Exception {
        mockMvc.perform(get("/api/v1/friends/request")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(1));
    }

    @Test
    void getFriends() throws Exception {
        mockMvc.perform(get("/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(1));
    }

    @Test
    void deleteFriend() throws Exception {
        mockMvc.perform(delete("/api/v1/friends/2"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(0));
    }

    @Test
    void deleteSentRequest() throws Exception {
        Integer expectedFriendshipsAfterDeleting = 4;
        mockMvc.perform(delete("/api/v1/friends/4"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertEquals(expectedFriendshipsAfterDeleting, friendshipsRepository.findAll().size());
    }
}
