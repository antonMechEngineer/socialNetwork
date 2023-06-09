package socialnet.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import socialnet.api.response.CommonRs;
import socialnet.api.response.GeolocationRs;
import socialnet.service.GeolocationsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geolocations")
@RequiredArgsConstructor
@Tag(name = "geolocations-controller", description = "Create or get records about cities and countries")
public class GeolocationsController {

    private final GeolocationsService geolocationsService;

    @GetMapping("/countries")
    public CommonRs<List<GeolocationRs>> getCountries() {

        return geolocationsService.getAllCountries();
    }

    @GetMapping("/cities/uses")
    public CommonRs<List<GeolocationRs>> getCitiesFromPersons(@RequestParam String country) throws Exception {

        return geolocationsService.getCitiesByCountryFromUsers(country);
    }

    @GetMapping("/cities/db")
    public CommonRs<List<GeolocationRs>> getCitiesFromDbStartsWith(@RequestParam String starts, @RequestParam String country) throws Exception {

        return geolocationsService.getAllCitiesByCountryStartsWithFromDb(country, starts);
    }

    @GetMapping("/cities/api")
    public CommonRs<List<GeolocationRs>> getCitiesFromApiStartsWith(@RequestParam String starts, @RequestParam String country) throws Exception {

        return geolocationsService.getAllCitiesByCountryStartsWithFromApi(country, starts);
    }
}
