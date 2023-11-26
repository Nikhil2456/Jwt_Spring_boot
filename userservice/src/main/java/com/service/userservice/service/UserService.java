package com.service.userservice.service;

import com.service.userservice.dto.UserDto;
import com.service.userservice.entity.User;
import com.service.userservice.repository.UserRepo;
import com.service.userservice.utils.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return modelMapperService.map(userRepo.save(modelMapperService.map(user, User.class)),UserDto.class);
    }

}
