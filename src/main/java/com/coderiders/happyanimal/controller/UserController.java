package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.UserRqDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "user-controller", description = "Пользователи")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //АДМИН, СУПЕРЮЗЕР или кто там хз
    @Operation(summary = "Добавление нового")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRsDto> addUser(@Valid @RequestBody UserRqDto userForm) {
        var created = userService.saveUser(userForm);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    //АДМИН
    @Operation(summary = "Все пользователи")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserRsDto> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    //АДМИН
    @Operation(summary = "Пользователь по его id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRsDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }
}