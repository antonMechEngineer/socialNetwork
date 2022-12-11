package main.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.PersonSettingsRequest;
import main.repository.PersonsRepository;
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

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Autowired
    private PersonsRepository personsRepository;

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


    @Test
    void getPersonSettings() throws Exception {
        mockMvc.perform(get("/api/v1/account/notifications"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data[0].type").value("POST"))
                .andExpect(jsonPath("$.data[1].type").value("POST_COMMENT"))
                .andExpect(jsonPath("$.data[2].type").value("COMMENT_COMMENT"))
                .andExpect(jsonPath("$.data[3].type").value("FRIEND_REQUEST"))
                .andExpect(jsonPath("$.data[4].type").value("MESSAGE"))
                .andExpect(jsonPath("$.data[5].type").value("FRIEND_BIRTHDAY"))
                .andExpect(jsonPath("$.data[6].type").value("POST_LIKE"));
    }

    @Test
    void editPersonSettings() throws Exception {
        PersonSettingsRequest request = new PersonSettingsRequest();
        request.setNotificationType("COMMENT_COMMENT");
        request.setEnable(false);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/account/notifications").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertFalse(personsRepository.findPersonById(1L).get().getPersonSettings().getCommentCommentNotification());
    }
}