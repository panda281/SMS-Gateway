package com.meinab.smsgateway.services;

import com.meinab.smsgateway.handlers.WebSocketHandlers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageSenderTest {
    @Mock
    private WebSocketHandlers mockHandler;
    @Mock
    private WebSocketSession mockWebSocketSession;
    @InjectMocks
    private MessageSender messageSender;

    @Test
    @DisplayName("send message")
    void testSendMessage() throws Exception {
        when(mockHandler.getSessionByUserId("testUser")).thenReturn(mockWebSocketSession);
        when(mockHandler.getSessionByUserId("testUser").isOpen()).thenReturn(true);

        messageSender.sendMessage("testUser","test 1,2,3");
        verify(mockWebSocketSession).sendMessage(new TextMessage("test 1,2,3"));

    }
}