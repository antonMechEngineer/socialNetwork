package soialNetworkApp.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import soialNetworkApp.api.request.FindPersonRq;
import soialNetworkApp.errors.EmptyFieldException;
import soialNetworkApp.service.search.SearchPersons;

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
}
