package com.cali.calibank.user.controller;

import com.cali.calibank.user.controller.UserDto.AuthRequest;
import com.cali.calibank.user.controller.UserDto.SignupRequest;
import com.cali.calibank.user.controller.UserDto.TokenResponse;
import com.cali.calibank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public TokenResponse signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/authenticate")
    public TokenResponse authenticate(@RequestBody AuthRequest request) {
        return userService.authenticate(request);
    }
}
