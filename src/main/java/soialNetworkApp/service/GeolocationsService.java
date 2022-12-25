package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.response.GeolocationResponse;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.errors.NoSuchEntityException;
import soialNetworkApp.model.entities.City;
import soialNetworkApp.model.entities.Country;
import soialNetworkApp.repository.CitiesRepository;
import soialNetworkApp.repository.CountriesRepository;
import soialNetworkApp.repository.PersonsRepository;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeolocationsService {

    private final CitiesRepository citiesRepository;
    private final CountriesRepository countriesRepository;
    private final PersonsRepository personsRepository;
    private final WeatherService weatherService;

    @Value("${socialNetwork.geolocation.countries-path}")
    private String countriesPath;
    @Value("${socialNetwork.geolocation.cities-path.main}")
    private String citiesPath1;
    @Value("${socialNetwork.geolocation.cities-path.params}")
    private String citiesPath2;
    @Value("${socialNetwork.geolocation.token}")
    private String token;
    @Value("${socialNetwork.geolocation.first-countries-in-response}")
    private List<String> firstCountriesInResponse;
    @Value("#{'${socialNetwork.geolocation.first-cities-in-response}'.split('; ')}")
    private List<String> firstCitiesInResponse;

    public void setCityGismeteoId(String cityName) throws Exception {
        List<String> cityFields = getCityFields(cityName);
        City city = citiesRepository.findCityByNameAndDistrictAndSubDistrict(cityFields.get(0), cityFields.get(1), cityFields.get(2))
                .orElseThrow(new NoSuchEntityException("City with " + cityName + " name was not found"));
        if (city.getGismeteoId() == null) {
            city.setGismeteoId(weatherService.getGismeteoCityId(city));
            if (city.getGismeteoId() != null) {
                citiesRepository.save(city);
            }
        }
    }

    public CommonResponse<List<GeolocationResponse>> getAllCountries() {
        List<Country> countries = countriesRepository.findAll();
        if (countries.isEmpty()) {
            countries.addAll(getCountriesFromApi());
        }
        return getResponse(countries.stream().map(Country::getName).collect(Collectors.toList()), firstCountriesInResponse);
    }

    private List<Country> getCountriesFromApi() {
        List<Country> response = new ArrayList<>();
        try {
            String jsonData = new String(new URL(countriesPath + token).openStream().readAllBytes());
            Set<String> jsonSet = new JSONObject(jsonData).keySet();
            jsonSet.forEach(key -> {
                if (key.matches("\\D+")){
                    return;
                }
                JSONObject countryData = new JSONObject(jsonData).getJSONObject(key);
                response.add(countriesRepository.save(
                        Country.builder()
                                .name(countryData.getString("name"))
                                .fullName(countryData.getString("fullname"))
                                .internationalName(countryData.getString("english"))
                                .codeTwoSymbols(countryData.getString("id"))
                                .build()));
            });
        } catch (IOException ignored) {}
        return response;
    }

    public CommonResponse<List<GeolocationResponse>> getCitiesByCountryFromUsers(String countryName) throws Exception {
        List<String> citiesNames = personsRepository.getCitiesByCountry(countryName);
        Country country = countriesRepository.findCountryByName(countryName)
                .orElseThrow(new NoSuchEntityException("Country with " + countryName + " name was not found"));
        List<City> cities = new ArrayList<>();
        country.getCities().forEach(city -> {
            if (citiesNames.stream().anyMatch(name -> name.equals(getCityFullName(city)))) {
                cities.add(city);
            }
        });
        return getResponse(cities.stream().map(this::getCityFullName).collect(Collectors.toList()), firstCitiesInResponse);
    }

    public CommonResponse<List<GeolocationResponse>> getAllCitiesByCountryStartsWithFromDb(String countryName, String startsWith) throws Exception {
        startsWith = startsWith.isEmpty() ?
                startsWith : startsWith.substring(0, 1).toUpperCase() + startsWith.substring(1);
        Country country = countriesRepository.findCountryByName(countryName)
                .orElseThrow(new NoSuchEntityException("Country with " + countryName + " name was not found"));
        List<City> cities = citiesRepository.findCitiesByCountryAndNameStartsWith(country, startsWith);
        return getResponse(cities.stream().map(this::getCityFullName).collect(Collectors.toList()), firstCitiesInResponse);
    }

    public CommonResponse<List<GeolocationResponse>> getAllCitiesByCountryStartsWithFromApi(String countryName, String startsWith) throws Exception {
        Country country = countriesRepository.findCountryByName(countryName)
                .orElseThrow(new NoSuchEntityException("Country with " + countryName + " name was not found"));
        List<GeolocationResponse> response = new ArrayList<>();
        try {
            String jsonData = new String(new URL(citiesPath1 + startsWith + citiesPath2 + token).openStream().readAllBytes());
            Set<String> jsonSet = new JSONObject(jsonData).keySet();
            jsonSet.forEach(key -> {
                if (key.matches("\\D+")){
                    System.out.println(key + " - " + new JSONObject(jsonData).get(key));
                    return;
                }
                JSONObject cityData = new JSONObject(jsonData).getJSONObject(key);
                if (cityData.getString("country").equals(country.getCodeTwoSymbols())) {
                    List<String> cityFields = getCityFields(cityData.getString("full_name"));
                    City city = citiesRepository.findCityByNameAndDistrictAndSubDistrict(cityFields.get(0), cityFields.get(1), cityFields.get(2)).orElse(
                            citiesRepository.save(City.builder()
                                    .country(countriesRepository.findCountryByCodeTwoSymbols(cityData.getString("country")).get())
                                    .name(cityData.getString("name"))
                                    .district(cityFields.get(1))
                                    .subDistrict(cityFields.get(2))
                                    .build()));
                    response.add(GeolocationResponse.builder()
                            .title(getCityFullName(city))
                            .build());
                }
            });
        } catch (IOException ignored) {}
        return CommonResponse.<List<GeolocationResponse>>builder().total((long) response.size()).data(response).build();
    }

    private List<GeolocationResponse> setFirstsInResponse(List<GeolocationResponse> geolocationResponses, List<String> firstsInResponse) {
        List<GeolocationResponse> response = new ArrayList<>(geolocationResponses);
        for (int i = firstsInResponse.size(); i > 0; i--) {
            int locationPosition = response.indexOf(GeolocationResponse.builder().title(firstsInResponse.get(i - 1)).build());
            if (locationPosition > -1) {
                GeolocationResponse geolocationResponse = response.get(locationPosition);
                response.remove(geolocationResponse);
                response.add(0, geolocationResponse);
            }
        }
        return response;
    }

    public static List<String> getCityFields(String fullName) {
        List<String> fields = new ArrayList<>();
        if (fullName != null) {
            int cityNameEndingIndex = fullName.indexOf(" (");
            if (cityNameEndingIndex > -1) {
                fields.add(fullName.substring(0, cityNameEndingIndex));
                fields.addAll(List.of(fullName.substring(cityNameEndingIndex + 2, fullName.length() - 1).split(", ")));
            }
            else {
                fields.add(fullName);
            }
        }
        while (fields.size() < 3) {
            fields.add("");
        }
        return fields;
    }

    private String getCityFullName(City city) {
        return city.getName() + " (" + city.getDistrict() +
                (city.getSubDistrict().isEmpty() ? "" : ", " + city.getSubDistrict()) + ")";
    }

    private CommonResponse<List<GeolocationResponse>> getResponse(List<String> titles, List<String> firstsInResponse) {
        List<GeolocationResponse> response = new ArrayList<>();
        titles.forEach(title -> response.add(GeolocationResponse.builder().title(title).build()));
        response.sort(Comparator.comparing(GeolocationResponse::getTitle));
        return CommonResponse.<List<GeolocationResponse>>builder()
                .total((long) response.size())
                .data(setFirstsInResponse(response, firstsInResponse))
                .build();
    }
}
