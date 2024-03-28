package com.meinab.smsgateway.services;

import com.meinab.smsgateway.models.Users;
import com.meinab.smsgateway.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }

    public void createUpdateUser(Users users) {
        usersRepository.save(users);
    }

    public Users loadUserByUsername(String userName) {
        return usersRepository.findFirstByUserName(userName).orElseThrow(() -> new IllegalArgumentException("Invalid user name or password"));
    }

}
