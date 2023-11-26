package com.service.userservice.service;

import com.service.userservice.dto.UserDto;
import com.service.userservice.entity.User;
import com.service.userservice.repository.UserRepo;
import com.service.userservice.utils.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapperService modelMapperService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = modelMapperService.map(userRepo.findByUsername(username),UserDto.class);

        if(user==null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
