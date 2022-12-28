package soialNetworkApp.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountUsers.sql")
    void getCountUsers() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/user"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("8"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountUsersByCountry.sql")
    void getCountUsersByCountry() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/user/country")
                        .param("country", "France"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("4"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountUsersByCity.sql")
    void getCountUsersByCity() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/user/city")
                        .param("city", "Москва"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("4"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountCities.sql")
    void getCountCities() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/city"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("8"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCitiesWithCountUsers.sql")
    void getCitiesWithCountUsers() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/city/all"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("[{\"region\":\"Paris\",\"countUsers\":4},{\"region\":\"Москва\",\"countUsers\":4}]"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountCountries.sql")
    void getCountCountries() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/country"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("8"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountriesWithCountUsers.sql")
    void getCountriesWithCountUsers() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/country/all"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("[{\"region\":\"France\",\"countUsers\":4},{\"region\":\"Россия\",\"countUsers\":4}]"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountDialogs.sql")
    void getCountDialogs() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/dialog"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("4"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountDialogsByUserId.sql")
    void getCountDialogsByUserId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/dialog/user")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("1"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountLikes.sql")
    void getCountLikes() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/like"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountLikesByEntityId.sql")
    void getCountLikesByEntityId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/like/entity")
                        .param("entityId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountMessages.sql")
    void getCountMessages() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/message"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountMessagesByDialogId.sql")
    void getCountMessagesByDialogId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/message/dialog")
                        .param("dialogId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountMessagesByTwoUsers.sql")
    void getCountMessagesByTwoUsers() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/message/all")
                        .param("firstUserId", "1")
                        .param("secondUserId", "2"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"author id - 1, recipient - 2\":6,\"author id - 2, recipient - 1\":0}"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountPosts.sql")
    void getCountPosts() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/post"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountPostsByUserId.sql")
    void getCountPostsByUserId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/post/user")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("6"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountCommentsByPostId.sql")
    void getCountCommentsByPostId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/comment/post")
                        .param("postId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("1"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountTags.sql")
    void getCountTags() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/tag"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("3"));
    }

    @Test
    @Sql("/StatisticsControllerData/statisticsController-getCountTagsByPostId.sql")
    void getCountTagsByPostId() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/tag/post")
                        .param("postId", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("3"));
    }
}