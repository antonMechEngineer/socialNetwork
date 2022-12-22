package main.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.*;
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
import java.util.LinkedHashMap;

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
@WithUserDetails("testbd@internet.ru")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRegResponse() throws Exception{
        RegisterRq registerRq = new RegisterRq();
        registerRq.setEmail("a@b.ru");
        registerRq.setFirstName("Peter");
        registerRq.setLastName("Second");
        registerRq.setCode("code");
        registerRq.setCodeSecret("secret");
        registerRq.setPasswd1("parole");
        registerRq.setPasswd2("parole");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(registerRq);
        mockMvc.perform(post("/api/v1/account/register").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").value("a@b.ru"));
    }

    @Test
    void getPasswordSet() throws Exception{
        PasswordSetRq passwordSetRq = new PasswordSetRq();
        passwordSetRq.setPassword("parole");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(passwordSetRq);
        mockMvc.perform(put("/api/v1/account/password/set").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordReSet() throws Exception{
        PasswordRq passwordRq = new PasswordRq();
        passwordRq.setPassword("newPass");
        passwordRq.setSecret("changeword3");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(passwordRq);
        mockMvc.perform(put("/api/v1/account/password/reset").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPasswordChange() throws Exception{
        LinkedHashMap email = new LinkedHashMap();
        email.put("email","testbd@internet.ru");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(email);
        mockMvc.perform(put("/api/v1/account/password/recovery").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailSet() throws Exception{//Скорректировать!!!
        EmailRq emailRq = new EmailRq();
        emailRq.setEmail("testbd@internet.ru");
        emailRq.setSecret("changeword3");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(emailRq);
        mockMvc.perform(put("/api/v1/account/email").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getEmailChange() throws Exception{
        EmailRq emailRq = new EmailRq();
        emailRq.setEmail("testbd@internet.ru");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(emailRq);
        mockMvc.perform(put("/api/v1/account/email/recovery").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}