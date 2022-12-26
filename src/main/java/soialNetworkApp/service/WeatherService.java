package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.WeatherRs;
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
import java.util.List;

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

    public Integer getGismeteoCityId(City city) {
//        RestTemplate template = new RestTemplate();
//        String qwe = new RestTemplate().getForObject(cityIdPath, String.class, Map.of(header, token));
//        HttpRequest request = HttpRequest.newBuilder().header(header, token).uri(new URI(cityIdPath + "киров")).build();
//        String jsonData = new String(new URL(cityIdPath + "киров").openStream());
//        System.out.println("><>< " + qwe);

//        URLConnection connection = new URL("https://www.cbr-xml-daily.ru/daily_json.js").openConnection();
//        connection.addRequestProperty(header, token);
//        String jsonData = new String(connection.getInputStream().readAllBytes());
//        System.out.println("><>< " + jsonData);


       String cityQuery = city.getName() + " " + city.getDistrict() +
               (city.getSubDistrict().isEmpty() ? "" : " " + city.getSubDistrict());
        Integer cityId = null;
        try {
            URLConnection connection = new URL(cityIdPath + cityQuery).openConnection();
            connection.addRequestProperty(header, token);
            String jsonData = new String(connection.getInputStream().readAllBytes());
            cityId = new JSONObject(jsonData).getInt("id");
            System.out.println("><>< " + jsonData);
        } catch (IOException ignored) {}
        return cityId;

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
    public WeatherRs getWeatherResponse(String cityName) {
                WeatherRs weatherRs = new WeatherRs();
        if (cityName != null) {
            List<String> cityFields = GeolocationsService.getCityFields(cityName);
            City city = citiesRepository.findCityByNameAndDistrictAndSubDistrict(
                    cityFields.get(0), cityFields.get(1), cityFields.get(2)).orElse(new City());
            if (city.getGismeteoId() != null) {
                weatherRepository.findFirstByGismeteoIdOrderByTimeDesc(city.getGismeteoId()).ifPresent(weather -> {
                    weatherRs.setCity(cityName);
                    weatherRs.setTemp(String.valueOf(weather.getTemperature()));
                    weatherRs.setClouds(weather.getWeatherDescription());
                    weatherRs.setDate(String.valueOf(weather.getTime()));
                });
            }
        }
        return weatherRs;
    }
}
