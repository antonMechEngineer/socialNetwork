package soialNetworkApp.mappers;

import soialNetworkApp.api.response.CurrencyResponse;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.api.response.WeatherResponse;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target = "city", source = "person.city.")
    @Mapping(target = "country", source = "person.country.")
    @Mapping(target = "weather", source = "person")
    @Mapping(target = "currency", source = "person")
    @Mapping(target = "friendStatus", source = "friendshipStatusTypes")
    @Mapping(target = "online", expression = "java(true)")
    @Mapping(target = "token", ignore = true)
    PersonResponse toFriendResponse(Person person, FriendshipStatusTypes friendshipStatusTypes);


    default WeatherResponse getMapWeather(Person person) {
        return WeatherResponse.builder()
                .clouds("clouds")
                .temp("9")
                .city(person.getCity() == null ? null : person.getCity())
                .build();
    }

    default CurrencyResponse getMapCurrency(Person person) {
        return CurrencyResponse.builder()
                .usd("60")
                .euro("62")
                .build();
    }
    default FriendshipStatusTypes getFriendStatus(FriendshipStatusTypes friendshipStatusTypes) {
        return friendshipStatusTypes;
    }


}

