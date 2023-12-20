package com.kravchenko.photoapp.api.users.ui.controllers;

import com.kravchenko.photoapp.api.users.service.UsersService;
import com.kravchenko.photoapp.api.users.shared.UserDto;
import com.kravchenko.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.kravchenko.photoapp.api.users.ui.model.CreateUserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    private Environment environment;

    @Autowired
    private UsersService usersService;

    @GetMapping(path = "/status/check")
    public String status() {
        return "Working on port = " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userRequestModel) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto createdUser =  usersService.createUser(userDto);

        CreateUserResponseModel createUserResponseModel = modelMapper.map(createdUser, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponseModel);
    }
}
