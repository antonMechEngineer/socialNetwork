package main.mappers;

import main.api.response.CurrencyRateRs;
import main.api.response.PersonResponse;
import main.api.response.WeatherRs;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target = "city", source = "person.city.title")
    @Mapping(target = "country", source = "person.city.country.title")
    @Mapping(target = "weather", source = "person")
    @Mapping(target = "currency", source = "person")
    @Mapping(target = "friendStatus", source = "friendshipStatusTypes")
    @Mapping(target = "online", expression = "java(true)")
    @Mapping(target = "token", ignore = true)
    PersonResponse toFriendResponse(Person person, FriendshipStatusTypes friendshipStatusTypes);


    default WeatherRs getMapWeather(Person person) {
        return WeatherRs.builder()
                .clouds("clouds")
                .temp("9")
                .city(person.getCity() == null ? null : person.getCity().getTitle())
                .build();
    }

    default CurrencyRateRs getMapCurrency(Person person) {
        return CurrencyRateRs.builder()
                .usd("60")
                .euro("62")
                .build();
    }
    default FriendshipStatusTypes getFriendStatus(FriendshipStatusTypes friendshipStatusTypes) {
        return friendshipStatusTypes;
    }


}

