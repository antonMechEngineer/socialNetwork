package soialNetworkApp.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import soialNetworkApp.api.response.WeatherRs;
import soialNetworkApp.model.entities.City;
import soialNetworkApp.model.entities.Weather;
import soialNetworkApp.repository.CitiesRepository;
import soialNetworkApp.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private CitiesRepository citiesRepository;
    @MockBean
    private WeatherRepository weatherRepository;

    private City city;
    private Weather weather;
    private final LocalDateTime time = LocalDateTime.of(2022, 12, 10, 12, 0, 0);
    private final String weatherDescription ="sunny";
    private final double temperature = 0;

    @BeforeEach
    void setUp() {
        city = new City();
        city.setName("city");
        city.setGismeteoId(1);
        weather = new Weather();
        weather.setWeatherDescription(weatherDescription);
        weather.setTime(time);
        weather.setGismeteoId(city.getGismeteoId());
        weather.setTemperature(temperature);
    }

    @AfterEach
    void tearDown() {
        city = null;
        weather = null;
    }

    @Test
    void getWeatherResponse() {
        when(citiesRepository.findCityByNameAndDistrictAndSubDistrict(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(city));
        when(weatherRepository.findFirstByGismeteoIdOrderByTimeDesc(anyInt())).thenReturn(Optional.of(weather));

        WeatherRs weatherRs = weatherService.getWeatherResponse("city");
        verify(citiesRepository).findCityByNameAndDistrictAndSubDistrict(anyString(), anyString(), anyString());
        verify(weatherRepository).findFirstByGismeteoIdOrderByTimeDesc(anyInt());
        assertEquals(weatherDescription, weatherRs.getClouds());
        assertEquals(String.valueOf(temperature), weatherRs.getTemp());
        assertEquals(time.toString(), weatherRs.getDate());
    }
}