package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.WeatherResponse;
import soialNetworkApp.model.entities.Weather;
import soialNetworkApp.model.entities.City;
import soialNetworkApp.repository.CitiesRepository;
import soialNetworkApp.repository.WeatherRepository;
import org.cloudinary.json.JSONObject;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final CitiesRepository citiesRepository;
    private final WeatherRepository weatherRepository;

    @Value("${socialNetwork.weather.city-id-path}")
    private String cityIdPath;
    @Value("${socialNetwork.weather.data-path}")
    private String dataPath;
    @Value("${socialNetwork.weather.token.header}")
    private String header;
    @Value("${socialNetwork.weather.token.value}")
    private String token;
    @Value("${socialNetwork.timezone}")
    private String timezone;

    public void setGismeteoCityId(String cityTitle) {
//        RestTemplate template = new RestTemplate();
//        String qwe = new RestTemplate().getForObject(cityIdPath, String.class, Map.of(header, token));
//        HttpRequest request = HttpRequest.newBuilder().header(header, token).uri(new URI(cityIdPath + "киров")).build();
//        String jsonData = new String(new URL(cityIdPath + "киров").openStream());
//        System.out.println("><>< " + qwe);

//        URLConnection connection = new URL("https://www.cbr-xml-daily.ru/daily_json.js").openConnection();
//        connection.addRequestProperty(header, token);
//        String jsonData = new String(connection.getInputStream().readAllBytes());
//        System.out.println("><>< " + jsonData);

        City city = citiesRepository.findCityByTitle(cityTitle).orElse(null);
        if (city == null) {
            city = new City();
            city.setTitle(cityTitle);
        }
        if (city.getGismeteoId() == null) {
            try {
                URLConnection connection = new URL(cityIdPath + cityTitle).openConnection();
                connection.addRequestProperty(header, token);
                String jsonData = new String(connection.getInputStream().readAllBytes());
                int cityId = new JSONObject(jsonData).getInt("id");
                city.setGismeteoId(cityId);
                citiesRepository.save(city);

                System.out.println("><>< " + jsonData);
            } catch (IOException ignored) {
            }
        }

//        String result = Request.Get("https://www.cbr-xml-daily.ru/daily_json.js")
////                .setHeader("Authorization", token)
//                .execute()
//                .returnContent().toString();
//        System.out.println("><><" + result);
    }

    @Scheduled(cron = "${socialNetwork.scheduling.weather}", zone = "${socialNetwork.timezone}")
    public void getWeather() {
        citiesRepository.findGismeteoIds().forEach(id -> {
            try {
            URLConnection connection = new URL(dataPath + id + "/").openConnection();
            connection.addRequestProperty(header, token);
            String jsonData = new String(connection.getInputStream().readAllBytes());
            Weather weather = new Weather();
            weather.setGismeteoId(id);
//            weather.setTime(ZonedDateTime.parse(new JSONObject(
//                    new JSONObject(jsonData).getString("date")).getString("UTC")).toLocalDateTime());
            weather.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(
                    new JSONObject(new JSONObject(jsonData).getString("date")).getLong("unix")),
                    ZoneId.of(timezone)));
            weather.setWeatherDescription(new JSONObject(
                    new JSONObject(jsonData).getString("description")).getString("full"));
            weather.setTemperature(new JSONObject(
                    new JSONObject(jsonData).getString("temperature")).getDouble("air"));

            System.out.println("><>< " + jsonData);
            } catch (IOException ignored) {}
        });
    }

    @Named("getWeatherResponse")
    public WeatherResponse getWeatherResponse(String cityTitle) {
        WeatherResponse weatherResponse = new WeatherResponse();
        if (cityTitle != null) {
            City city = citiesRepository.findCityByTitle(cityTitle).orElse(new City());
            if (city.getGismeteoId() != null) {
                weatherRepository.findTopByGismeteoId(city.getGismeteoId()).ifPresent(weather -> {
                    weatherResponse.setCity(cityTitle);
                    weatherResponse.setTemp(String.valueOf(weather.getTemperature()));
                    weatherResponse.setClouds(weather.getWeatherDescription());
                    weatherResponse.setDate(String.valueOf(weather.getTime()));
                });
            }
        }
        return weatherResponse;
    }
}
