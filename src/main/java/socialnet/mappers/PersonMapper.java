package socialnet.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import socialnet.api.response.PersonRs;
import socialnet.model.entities.Person;
import socialnet.service.CurrenciesService;
import socialnet.service.WeatherService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", uses = {CurrenciesService.class, WeatherService.class})
public interface PersonMapper {

    @Mapping(target = "weather", source = "city", qualifiedByName = "getWeatherResponse")
    @Mapping(target = "currency", source = "person", qualifiedByName = "getCurrencies")
    @Mapping(target = "online", source = "person")
    @Mapping(target = "token", ignore = true)
    PersonRs toPersonResponse(Person person);

    default boolean getOnlineStatus(Person person) {
        return person.getLastOnlineTime() != null &&
                LocalDateTime.now(ZoneId.of("Europe/Moscow")).minus(1, ChronoUnit.MINUTES)
                        .isBefore(person.getLastOnlineTime());
    }
}
