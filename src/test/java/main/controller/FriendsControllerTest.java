package main.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.CommentRequest;
import main.api.request.PostRequest;
import main.service.PersonsService;
import org.aspectj.weaver.ast.ITestVisitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@Sql("/test-data.sql")
@AutoConfigureMockMvc
@WithUserDetails("rhoncus.nullam@yahoo.edu")
class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addFriend() throws Exception {
         mockMvc.perform(post("/api/v1/friends/2").
                         contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeader()))
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful());
    }

    @Test
    void sendFriendshipRequest() throws Exception {
        mockMvc.perform(post("/api/v1/friends/request/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPotentialFriends() throws Exception {
        mockMvc.perform(get("/api/v1/friends/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getFriends() throws Exception {
        mockMvc.perform(get("/api/v1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void deleteFriend() throws Exception {
        mockMvc.perform(delete("/api/v1/friends/3")
                        .headers(buildHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteSentRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/friends/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }


    private HttpHeaders buildHeader(){
        String nameHeader = "Authorization";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(nameHeader, token);
        return httpHeaders;
    }

}
