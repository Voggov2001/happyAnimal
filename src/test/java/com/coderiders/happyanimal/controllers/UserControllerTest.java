package com.coderiders.happyanimal.controllers;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.model.dto.UserRqDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper myObjectMapper;

    @Autowired
    private WebApplicationContext context;

    private static ObjectMapper objectMapper;
    private static final String DEFAULT_STRING = "string";
    private static final int DEFAULT_INT = 1;
    private static final String DEFAULT_URL_PATH = "/users";

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

    @Before
    public void addUser() {
        var userRqDto = getUserRqDto();
        userService.saveUser(userRqDto);
    }

    @Test
    void addUserTest() throws Exception {
        final var userRqDto = getUserRqDto();
        mockMvc.perform(postJson(userRqDto))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", equalTo(DEFAULT_STRING)))
                .andExpect(jsonPath("lastName", equalTo(DEFAULT_STRING)))
                .andExpect(jsonPath("patronymic", equalTo(DEFAULT_STRING)))
                .andExpect(jsonPath("userRole", equalTo(UserRole.ADMIN.name())))
                .andExpect(jsonPath("age", equalTo(DEFAULT_INT)))
                .andExpect(jsonPath("login", equalTo(DEFAULT_STRING)));
    }

    @Test
    void addEmptyUser() throws Exception {
        mockMvc.perform(postJson(null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addNotValidUser() throws Exception {
        var userRqDto = getUserRqDto();
        userRqDto.setName(null);
        mockMvc.perform(postJson(userRqDto))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getUserById() throws Exception {
        var userRsDto = getUserRsDto();
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{id}", 2L))
                .andDo(print())
                .andExpect(jsonPath("id", equalTo(userRsDto.getId().intValue())))
                .andExpect(jsonPath("name", equalTo(userRsDto.getName())))
                .andExpect(jsonPath("lastName", equalTo(userRsDto.getLastName())))
                .andExpect(jsonPath("patronymic", equalTo(userRsDto.getPatronymic())))
                .andExpect(jsonPath("userRole", equalTo(userRsDto.getUserRole().name())))
                .andExpect(jsonPath("age", equalTo(userRsDto.getAge())))
                .andExpect(jsonPath("login", equalTo(userRsDto.getLogin())));
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getUserByNullId() throws Exception {
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{id}", 0))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getUserByIncorrectId() throws Exception {
        mockMvc.perform(get(DEFAULT_URL_PATH + "/{id}", 100000L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "admin")
    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(get(DEFAULT_URL_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @After
    public void deleteUser() {
        userRepository.deleteById(2L);
    }

    private MockHttpServletRequestBuilder postJson(Object body, Object... uriVars) {
        try {
            return post(UserControllerTest.DEFAULT_URL_PATH, uriVars)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UserRqDto getUserRqDto() {
        return UserRqDto.builder()
                .name(DEFAULT_STRING)
                .lastName(DEFAULT_STRING)
                .patronymic(DEFAULT_STRING)
                .age(DEFAULT_INT)
                .userRole(UserRole.ADMIN)
                .login(DEFAULT_STRING)
                .password(DEFAULT_STRING)
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
}