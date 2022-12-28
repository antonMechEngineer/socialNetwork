package soialNetworkApp.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.util.CurrentUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class FriendsRecommendationServiceTest {

    @Autowired
    private FriendsRecommendationService friendsRecommendationService;

    @MockBean
    private PersonsRepository personsRepository;

    @MockBean
    private CurrentUser currentUser;

    @Test
    void getFriendsRecommendation(){
        Person person = new Person();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(personsRepository.getPersonByCityAndIdNotIn(any(), any(), any())).thenReturn(Page.empty());
        when(personsRepository.getPersonByIdNotInOrderByRegDateDesc(any(), any())).thenReturn(Page.empty());
        when(currentUser.getPerson()).thenReturn(person);

        friendsRecommendationService.getFriendsRecommendation();

        verify(personsRepository, times(1)).getPersonByCityAndIdNotIn(any(), any(), any());
        verify(personsRepository, times(1)).getPersonByIdNotInOrderByRegDateDesc(any(),any());
    }
}