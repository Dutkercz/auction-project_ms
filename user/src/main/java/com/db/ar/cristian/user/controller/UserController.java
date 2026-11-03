package com.db.ar.cristian.user.controller;

import com.db.ar.cristian.user.dto.UserRequest;
import com.db.ar.cristian.user.dto.UserResponse;
import com.db.ar.cristian.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request,
                                                   UriComponentsBuilder builder){
        var response = userService.createUser(request);
        URI uri = builder.path("api/v1/users/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

}
