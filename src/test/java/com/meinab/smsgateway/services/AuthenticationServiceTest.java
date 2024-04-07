package com.meinab.smsgateway.services;

import com.meinab.smsgateway.models.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
@Mock
private UsersService usersService;
@Mock
private PasswordEncoder passwordEncoder;
@InjectMocks
private AuthenticationService authenticationService;

    @Test
    @DisplayName("Given a basicAuth String, when the password is correct, then it returns username")
    void givenBasicAuthString_WhenThePasswordIsCorrect_ThenItReturnsUsername(){
        Users users = Users.builder().isActive(true).userName("admin").password("password").id(1).build();
        when(usersService.loadUserByUsername("admin")).thenReturn(users);
        when(passwordEncoder.matches("admin","password")).thenReturn(true);
        assertEquals("admin",authenticationService.login("Basic YWRtaW46YWRtaW4="));
    }
    @Test
    @DisplayName("Given a basicAuth String, when the password is not correct, then throw BadCredential Exception")
    void givenBasicAuthString_WhenThePasswordIsNotCorrect_ThenThrowBadCredentialException() {
        Users users = Users.builder().isActive(true).userName("admin").password("password").id(1).build();
        when(usersService.loadUserByUsername("admin")).thenReturn(users);
        assertThrows(BadCredentialsException.class,()->authenticationService.login("Basic YWRtaW46YWRtaW4="));
    }

    @Test
    @DisplayName("Given a basicAuthString, when the basicAuthString is not a correct basicAuthToken, then throw IllegalArgumentException EX1")
    void givenBasicAuthString_WhenTheBasicAuthStringIsNotCorrect_ThenThrowIllegalArgumentExceptionEx1(){
        assertThrows(IllegalArgumentException.class,()->authenticationService.login("Basic RtaW4="));
    }



    @Test
    @DisplayName("Given a basicAuthString, when the basicAuthString is null, then throw IllegalArgumentException")
    void givenBasicAuthString_WhenTheBasicAuthStringIsNull_ThenThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class,()->authenticationService.login(null));
    }

    @Test
    @DisplayName("Given a basicAuthString, when the basicAuthString token only with out the basic keyword, then throw IllegalArgumentException")
    void givenBasicAuthString_WhenTheBasicAuthStringTokenOnlyWithOutTheBasicKeyword_ThenThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class,()->authenticationService.login("YWRtaW46YWRtaW4="));
    }

    @Test
    @DisplayName("Given a basicAuthString, when the basicAuthString token contains more than 2 keys inside the token, then throw IllegalArgumentException")
    void givenBasicAuthString_WhenTheBasicAuthStringTokenContainsMoreThan2KeysInsideTheToken_ThenThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class,()->authenticationService.login("Basic YWRtaW46YWRtaW46YWRtaW4="));
    }
}