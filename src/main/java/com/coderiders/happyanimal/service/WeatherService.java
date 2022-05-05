package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.mapper.WeatherMapper;
import com.coderiders.happyanimal.model.dto.WeatherDto;
import com.coderiders.happyanimal.model.dto.WeatherFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final WeatherMapper weatherMapper;
    private static final String API_KEY = "6c51e668f89345a6a52112444220403";

    @Autowired
    public WeatherService(RestTemplate restTemplate, WeatherMapper weatherMapper) {
        this.restTemplate = restTemplate;
        this.weatherMapper = weatherMapper;
    }

    public WeatherDto getWeatherForecast(int countOfDays) {
        ResponseEntity<WeatherFromJson> weather = restTemplate.getForEntity(
                "http://api.weatherapi.com/v1/forecast.json?key=" +
                        API_KEY +
                        "&q=Penza&" +
                        "hour=0" +
                        "&lang=ru" +
                        "&days=" +
                        countOfDays,
                WeatherFromJson.class);
        WeatherFromJson weatherFromJson = Objects.requireNonNull(weather.getBody());
        return weatherMapper.mapWeatherFromJsonToWeatherDto(weatherFromJson);
    }
}