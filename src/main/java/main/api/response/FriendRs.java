package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class FriendRs {
    String error;
    Timestamp timestamp;
    ComplexRs data;
}

//getFriends
//{
//        "error": "string",
//        "timestamp": 0,
//        "total": 0,
//        "offset": 0,
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


//request
//{
//        "error": "string",
//        "timestamp": 0,
//        "total": 0,
//        "offset": 0,
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


//recommendation
//{
//        "error": "string",
//        "timestamp": 0,
//        "total": 0,
//        "offset": 0,
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