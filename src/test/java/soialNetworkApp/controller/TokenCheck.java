package soialNetworkApp.controller;

import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TokenCheck {
    @Value("${token.wrongSecretToken}")
    private String wrongSecretToken;
    @Value("${token.expiredToken}")
    private String expiredToken;

    void wrongOrExpiredTokenCheck(MockMvc mockMvc, HttpMethod httpMethod, String url) throws Exception {
        mockMvc.perform(request(httpMethod, url)
                        .header("Authorization", expiredToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        Throwable thrown = assertThrows(SignatureException.class, () -> mockMvc.perform(request(httpMethod, url)
                .header("Authorization", wrongSecretToken)));
        assertNotNull(thrown.getMessage());
    }
}
