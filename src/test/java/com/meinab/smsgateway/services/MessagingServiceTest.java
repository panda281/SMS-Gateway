package com.meinab.smsgateway.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meinab.smsgateway.dto.MessageResponseModel;
import com.meinab.smsgateway.dto.MessagingDto;
import com.meinab.smsgateway.models.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MessagingServiceTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MessageSender messageSender;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private MessagingService messageService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Given a MessagingDto object, when the MessagingDto object contains phoneNo and Message, then it sends the message to websocket")
    void testSendMessage() throws IOException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String username = "user1";
        String phoneNo = "12345678";
        String messageText = "Hello, World!";
        Users user = mock(Users.class);
        MessagingDto messagingDto = new MessagingDto();
        messagingDto.setPhoneNo(phoneNo);
        messagingDto.setMessage(messageText);

        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getUsername()).thenReturn(username);
        when(objectMapper.writeValueAsString(any(Map.class))).thenReturn("jsonMessage");

        MessageResponseModel result = messageService.sendMessage(messagingDto);

        assertNotNull(result);
        assertEquals(phoneNo, result.getPhoneNo());
        assertTrue(result.isSentSuccessfully());
    }
}