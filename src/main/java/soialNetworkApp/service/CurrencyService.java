package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.model.entities.Currency;
import soialNetworkApp.repository.CurrenciesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrenciesRepository currenciesRepository;

    public Currency getCurrencyByName(String name) {
        return currenciesRepository.findCurrenciesByName(name).orElse(new Currency());
    }

}
