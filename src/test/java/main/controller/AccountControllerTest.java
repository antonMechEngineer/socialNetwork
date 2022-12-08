package main.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.PostRequest;
import main.api.request.UserRq;
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
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRegResponse() throws Exception{
 //       mockMvc.perform(post("/api/v1/account/register"))
 //               .andDo(print())
 //               .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordSet() throws Exception{
 //       mockMvc.perform(put("/api/v1/account/password/set"))
 //               .andDo(print())
 //               .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordReSet() throws Exception{
//        mockMvc.perform(put("/api/v1/account/password/reset"))
 //               .andDo(print())
 //               .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordRecovery() throws Exception{
 //       mockMvc.perform(put("/api/v1/account/password/recovery"))
 //               .andDo(print())
 //               .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailChange() throws Exception{
 //       mockMvc.perform(put("/api/v1/account/email"))
 //               .andDo(print())
 //               .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailRecovery() throws Exception{
 //       mockMvc.perform(put("/api/v1/account/email/recovery"))
 //               .andDo(print())
  //              .andExpect(status().is2xxSuccessful());
    }
}