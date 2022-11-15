package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.entities.Person;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class FriendsRs {

    // TODO: 15.11.2022 существуют дополнительные поля типа token (м.б. это changePasswordToken), массив currency, массив weather (это должны обслуживать внешние сервисы) которые не привязаны к person их надо привязать?
    // TODO: 15.11.2022 user_deleted у нас называется isDeleted из-за этого может быть конфликт
    private String error;
    private LocalDateTime time;
    private int total;
    private List<Person> data;
    private int itemPerPage;
    @JsonProperty("error_description")
    private String errorDescription;

//        "data": [
//        {
//        "id": 0,
//        "email": "string",
//        "phone": "string",
//        "photo": "string",
//        "about": "string",
//        "city": "string",
//        "country": "string",
//        "token": "string",
//        "weather": {
//        "clouds": "string",
//        "temp": "string",
//        "city": "string"
//        },
//        "currency": {
//        "usd": "string",
//        "euro": "string"
//        },
//        "online": true,
//        "first_name": "string",
//        "last_name": "string",
//        "reg_date": 0,
//        "birth_date": 0,
//        "messages_permission": "ALL",
//        "last_online_time": 0,
//        "is_blocked": true,
//        "friend_status": "REQUEST",
//        "user_deleted": true
//        }
//        ],
//        "itemPerPage": 0,
//        "error_description": "string"
//        }


}
