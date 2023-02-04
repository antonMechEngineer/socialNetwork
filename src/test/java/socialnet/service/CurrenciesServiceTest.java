package socialnet.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import socialnet.model.entities.Currency;
import socialnet.model.entities.Person;
import socialnet.repository.CurrenciesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class CurrenciesServiceTest {

    @Autowired
    private CurrenciesService currenciesService;

    @MockBean
    private CurrenciesRepository currenciesRepository;

    private Currency usd;
    private Currency eur;

    @BeforeEach
    void setUp() {
        usd = new Currency();
        usd.setPrice("64.3015");
        eur = new Currency();
        eur.setPrice("68.447");
    }

    @AfterEach
    void tearDown() {
        usd = null;
        eur = null;
    }

    @Test
    void getCurrencies() {
        when(currenciesRepository.findFirstByNameOrderByUpdateTimeDesc("USD")).thenReturn(Optional.of(usd));
        when(currenciesRepository.findFirstByNameOrderByUpdateTimeDesc("EUR")).thenReturn(Optional.of(eur));

        assertEquals(usd.getPrice(), currenciesService.getCurrencies(new Person()).getUsd());
        assertEquals(eur.getPrice(), currenciesService.getCurrencies(new Person()).getEuro());
    }
}