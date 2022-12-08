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
        mockMvc.perform(post("/api/v1/account/register"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordSet() throws Exception{
        mockMvc.perform(put("/api/v1/account/password/set"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordReSet() throws Exception{
        mockMvc.perform(put("/api/v1/account/password/reset"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordRecovery() throws Exception{
        mockMvc.perform(put("/api/v1/account/password/recovery"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailChange() throws Exception{
        mockMvc.perform(put("/api/v1/account/email"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailRecovery() throws Exception{
        mockMvc.perform(put("/api/v1/account/email/recovery"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

//    @Test
//    void updateMyData() throws Exception{
//        UserRq userRq = new UserRq();
//        userRq.setPhone("79999999999");
//        userRq.setAbout("Just something");
//        userRq.setCity("Moscow");
//        userRq.setCountry("Russia");
//        userRq.setFirst_name("Peter");
//        userRq.setLast_name("First");
//        userRq.setBirth_date("1999-06-08T10:54:06+03:00");
//        userRq.setPhoto_id("https://res.cloudinary.com/dre3qhjvh/image/upload/v1669013824/default-1_wzqelg.png");
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson = ow.writeValueAsString(userRq);
//        mockMvc.perform(put("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON).content(requestJson))
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.data.phone").value("79999999999"))
//                .andExpect(jsonPath("$.data.about").value("Just something"))
//                .andExpect(jsonPath("$.data.city").value("Moscow"))
//                .andExpect(jsonPath("$.data.country").value("Russia"))
//                .andExpect(jsonPath("$.data.first_name").value("Peter"))
//                .andExpect(jsonPath("$.data.last_name").value("First"))
//                .andExpect(jsonPath("$.data.birth_date").value("1999-06-08T10:54:06"))
//                .andExpect(jsonPath("$.data.photo").value("https://res.cloudinary.com/dre3qhjvh/image/upload/v1669013824/default-1_wzqelg.png"));
//    }
//

}