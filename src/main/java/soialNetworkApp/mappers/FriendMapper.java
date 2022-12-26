package soialNetworkApp.mappers;

import soialNetworkApp.api.response.CurrencyRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.api.response.WeatherRs;
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
    PersonRs toFriendResponse(Person person, FriendshipStatusTypes friendshipStatusTypes);


    default WeatherRs getMapWeather(Person person) {
        return WeatherRs.builder()
                .clouds("clouds")
                .temp("9")
                .city(person.getCity() == null ? null : person.getCity())
                .build();
    }

    default CurrencyRs getMapCurrency(Person person) {
        return CurrencyRs.builder()
                .usd("60")
                .euro("62")
                .build();
    }
    default FriendshipStatusTypes getFriendStatus(FriendshipStatusTypes friendshipStatusTypes) {
        return friendshipStatusTypes;
    }


}

