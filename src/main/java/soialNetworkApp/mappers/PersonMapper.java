package soialNetworkApp.mappers;

import soialNetworkApp.api.response.CurrencyRateRs;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.api.response.WeatherRs;
import soialNetworkApp.model.entities.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "weather", source = "person")
    @Mapping(target = "currency", source = "person")
    @Mapping(target = "online", source = "person")
    @Mapping(target = "token", ignore = true)
    PersonResponse toPersonResponse(Person person);

    default WeatherRs getMapWeather(Person person) {
        return WeatherRs.builder()
                .clouds("clouds")
                .temp("9")
                .city(person.getCity() == null ? null : person.getCity())
                .build();
    }

    default CurrencyRateRs getMapCurrency(Person person) {
        return CurrencyRateRs.builder()
                .usd("60")
                .euro("62")
                .build();
    }

    default boolean getOnlineStatus(Person person) {
        return person.getLastOnlineTime() != null &&
                LocalDateTime.now(ZoneId.of("Europe/Moscow")).minus(1, ChronoUnit.MINUTES)
                        .isBefore(person.getLastOnlineTime());
    }
}
