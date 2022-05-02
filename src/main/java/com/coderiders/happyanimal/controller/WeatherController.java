package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.mapper.WeatherMapper;
import com.coderiders.happyanimal.model.dto.WeatherDto;
import com.coderiders.happyanimal.model.dto.WeatherFromJson;
import com.coderiders.happyanimal.service.WeatherService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "swagger_config")
@Tag(name = "weather-controller", description = "погода на следующие три дня")
public class WeatherController {
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    //ВСЕ
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WeatherDto getWeatherDto(@RequestParam int countOfDays) {
        return weatherService.getWeatherForecast(countOfDays);
    }
}