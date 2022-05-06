package com.coderiders.happyanimal.controllers;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.service.AnimalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureMockMvc(addFilters = false)
class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper myObjectMapper;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private WebApplicationContext context;

    private static ObjectMapper objectMapper;
    private static final String DEFAULT_STRING = "string";
    private static final int DEFAULT_INT = 1;
    private static final String DEFAULT_URL_PATH = "/animals";

    @PostConstruct
    private void initStaticObjectMapper() {
        objectMapper = this.myObjectMapper;
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @WithMockUser(authorities = "admin")
    @Test
    void addAnimalTest() throws Exception {
        final var animalRqDto = getAnimalRqDto();
        mockMvc.perform(postJson(animalRqDto))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addEmptyAnimal() throws Exception {
        mockMvc.perform(postJson(null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addNotValidAnimal() throws Exception {
        var animalRqDto = getAnimalRqDto();
        animalRqDto.setName(null);
        mockMvc.perform(postJson(animalRqDto))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @BeforeEach
    void addAnimal() {
        var animalRqDto = getAnimalRqDto();
        animalService.saveAnimal(animalRqDto);
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getAnimalById() throws Exception {
        var animalRsDto = getAnimalRsDto();
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{animalId}", 1L))
                .andDo(print())
                .andExpect(jsonPath("name", equalTo(animalRsDto.getName())))
                .andExpect(jsonPath("gender", equalTo(animalRsDto.getGender())))
                .andExpect(jsonPath("age", equalTo(animalRsDto.getAge())))
                .andExpect(jsonPath("height", equalTo(animalRsDto.getHeight())))
                .andExpect(jsonPath("weight", equalTo(animalRsDto.getWeight())))
                .andExpect(jsonPath("status", equalTo(animalRsDto.getStatus())))
                .andExpect(jsonPath("featuresOfKeeping", equalTo(animalRsDto.getFeaturesOfKeeping())))
                .andExpect(jsonPath("externalFeatures", equalTo(animalRsDto.getExternalFeatures())))
                .andExpect(jsonPath("location", equalTo(animalRsDto.getLocation())));
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getAnimalByNullId() throws Exception {
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{id}", 0))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getAnimalByIncorrectId() throws Exception {
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{id}", 100000L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @After
    public void deleteAnimal() {
        animalRepository.deleteById(1L);
    }

    private MockHttpServletRequestBuilder postJson(Object body, Object... uriVars) {
        try {
            return post(AnimalControllerTest.DEFAULT_URL_PATH, uriVars)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private AnimalRqDto getAnimalRqDto() {
        return AnimalRqDto.builder()
                .name(DEFAULT_STRING)
                .gender(DEFAULT_STRING)
                .age(DEFAULT_INT)
                .height(DEFAULT_INT)
                .weight(DEFAULT_INT)
                .kind("Бурый медведь")
                .status("Здоров")
                .userId(2L)
                .featuresOfKeeping(DEFAULT_STRING)
                .externalFeatures(DEFAULT_STRING)
                .location(DEFAULT_STRING)
                .build();
    }

    private AnimalRsDto getAnimalRsDto() {
        return AnimalRsDto.builder()
                .id(1L)
                .name(DEFAULT_STRING)
                .gender(DEFAULT_STRING)
                .age(DEFAULT_INT)
                .height(DEFAULT_INT)
                .weight(DEFAULT_INT)
                .animalKindDto(getAnimalKindDto())
                .status("Здоров")
                .featuresOfKeeping(DEFAULT_STRING)
                .externalFeatures(DEFAULT_STRING)
                .location(DEFAULT_STRING)
                .userRsDto(getUserRsDto())
                .build();
    }

    private UserRsDto getUserRsDto() {
        return UserRsDto.builder()
                .id(2L)
                .name(DEFAULT_STRING)
                .lastName(DEFAULT_STRING)
                .patronymic(DEFAULT_STRING)
                .age(DEFAULT_INT)
                .userRole(UserRole.ADMIN)
                .login(DEFAULT_STRING)
                .build();
    }

    private AnimalKindDto getAnimalKindDto() {
        return new AnimalKindDto("Млекопитающие", "Хищные", "Бурый медведь");
    }
}
/*
  "name": "string",
  "gender": "string",
  "age": 0,
  "height": 0,
  "weight": 0,
  "kind": "Бурый медведь",
  "status": "Здоров",
  "userId": 2,
  "featuresOfKeeping": "string",
  "externalFeatures": "string",
  "location": "string"
*/

/*"id": 2,
  "name": "string",
  "gender": "string",
  "age": 0,
  "height": 0,
  "weight": 0,
  "animalKindDto": {
    "animalClass": "Хищные",
    "squad": "Хищные",
    "kind": "Бурый медведь",
    "pic": "str"
  },
  "status": "Здоров",
  "location": "string",
  "animalStatus": null,
  "featuresOfKeeping": "string",
  "externalFeatures": "string",
  "userRsDto": {
    "id": 2,
    "name": "string",
    "lastName": "string",
    "patronymic": "string",
    "userRole": "ADMIN",
    "age": 0,
    "login": "cde"
  }*/