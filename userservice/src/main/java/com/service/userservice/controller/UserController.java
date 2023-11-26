package com.service.userservice.controller;

import com.service.userservice.config.JwtTokenProvider;
import com.service.userservice.dto.UserDto;
import com.service.userservice.exception.CustomException;
import com.service.userservice.service.JwtUserDetailService;
import com.service.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailService jwtUserDetailService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @PostMapping("/add")
    public String addNewUser(@RequestBody UserDto user){
        UserDto userDto = userService.createUser(user);
        return Map.of("status",true).toString();
    }
    @PostMapping("/authenticate")
    public Map<String,String> authenticateUser(@RequestBody UserDto userDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userDto.getUsername());
            String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

            return Map.of("token", token);
        }
        catch (Exception e){
            throw new CustomException("No User Exists");
        }
    }
}
