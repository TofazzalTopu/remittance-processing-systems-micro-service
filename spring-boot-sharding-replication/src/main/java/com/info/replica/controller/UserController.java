package com.info.replica.controller;

import com.info.replica.dto.UserDTO;
import com.info.replica.entity.User;
import com.info.replica.service.UserReadService;
import com.info.replica.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserReadService userReadService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Adds a new user to the system")
    public ResponseEntity<User> save(@Valid @RequestBody UserDTO user) throws URISyntaxException {
        return ResponseEntity.created(new URI("/users")).body(userService.save(user));
    }

    @ApiResponse(description = "User not found")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user by id", description = "Find a user by id")
    public ResponseEntity<?> findById(@PathVariable @NotNull Long userId) {
        Optional<User> user = userReadService.getUser(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<?> findByAll() {
        List<User> userList = userReadService.findAll();
        return ResponseEntity.ok(userList);
    }

}

