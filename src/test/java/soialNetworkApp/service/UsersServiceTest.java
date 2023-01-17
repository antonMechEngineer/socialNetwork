package soialNetworkApp.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.logging.log4j.CloseableThreadContext;
import org.aspectj.weaver.ast.Instanceof;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.InstanceField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import soialNetworkApp.api.request.FindPersonRq;
import soialNetworkApp.api.request.UserRq;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.errors.EmptyFieldException;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.search.SearchPersons;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @MockBean
    private SearchPersons searchPersons;

    @MockBean
    private PersonsRepository personsRepository;

    @MockBean
    private PersonMapper personMapper;

    @Test
    void findPersons() throws EmptyFieldException {
        FindPersonRq personRq = new FindPersonRq();
        personRq.setFirst_name("Name");
        when(searchPersons.findPersons(any(), anyInt(), anyInt())).thenReturn(Page.empty());
        usersService.findPersons(personRq, 0, 20);
        verify(searchPersons, times(1)).findPersons(personRq, 0, 20);
    }

    @Test
    void findPersonException() {
        FindPersonRq personRq = new FindPersonRq();
        Throwable thrown = catchThrowable(() -> usersService.findPersons(personRq, 0, 20));
        assertThat(thrown).isInstanceOf(EmptyFieldException.class);
        assertThat(thrown.getMessage()).isNotBlank();
    }

    @Test
    void testEditProfile() throws Exception {
        UserRq userRq = new UserRq();
        userRq.setAbout("about");
        Person person = new Person();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(personMapper.toPersonResponse(person)).thenReturn(PersonRs.builder().id(1L).build());
        assertThat(userRq.getAbout()).isEqualTo("about");
    }
}
