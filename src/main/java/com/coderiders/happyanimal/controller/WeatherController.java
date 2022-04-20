package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.mapper.WeatherMapper;
import com.coderiders.happyanimal.model.dto.WeatherDto;
import com.coderiders.happyanimal.model.dto.WeatherFromJson;
import com.coderiders.happyanimal.service.WeatherService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Hidden
@RestController
@RequestMapping("/weather")
@Tag(name = "weather-controller", description = "погода на следующие три дня")
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    @Autowired
    public WeatherController(WeatherService weatherService, WeatherMapper weatherMapper) {
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    //ВСЕ
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WeatherDto getWeatherDto(@RequestParam int countOfDays) {
        ResponseEntity<WeatherFromJson> weatherForecast = weatherService.getWeatherForecast(countOfDays);
        WeatherFromJson weatherFromJson = Objects.requireNonNull(weatherForecast.getBody());
        return weatherMapper.mapWeatherFromJsonToWeatherDto(weatherFromJson);
    }
}