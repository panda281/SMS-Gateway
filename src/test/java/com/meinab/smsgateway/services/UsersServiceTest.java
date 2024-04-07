package com.meinab.smsgateway.services;

import com.meinab.smsgateway.models.Users;
import com.meinab.smsgateway.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
@Mock
private UsersRepository usersRepository;

@InjectMocks
private UsersService usersService;
    @Test
    @DisplayName("Given a User Object, when the User object contains the correct values, it creates and saves the User to database")
    void createUpdateUser() {
        Users users = Users.builder().userName("test").password("test").isActive(true).build();
        usersService.createUpdateUser(users);
        verify(usersRepository).save(users);
    }
}