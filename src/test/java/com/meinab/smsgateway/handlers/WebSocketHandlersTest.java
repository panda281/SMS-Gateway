package com.meinab.smsgateway.handlers;

import com.meinab.smsgateway.exceptions.UserNotFound;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class WebSocketHandlersTest {
    @Mock
    private WebSocketSession mockSession;
    @Mock
    private CloseStatus closeStatus;
    @InjectMocks
    private WebSocketHandlers handlers;

    @Test
    @DisplayName("when the connection established")
    @Order(2)
    void testAfterConnectionEstablished() throws Exception {
        when(mockSession.getAttributes()).thenReturn(Map.of("userId","testUser"));
        handlers.afterConnectionEstablished(mockSession);
        assertEquals(mockSession, handlers.getSessionByUserId("testUser"));
        verify(mockSession, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("get session by id for existing user")
    @Order(3)
    void testGetSessionByUserIdForTheExistingUser(){
        assertDoesNotThrow(()->handlers.getSessionByUserId("testUser"));
    }

    @Test
    @DisplayName("get session by id for non-existing user")
    @Order(1)
    void testGetSessionByUserIdWhenForNewUser(){
        assertThrows(UserNotFound.class,()->handlers.getSessionByUserId("testUser"));
    }

    @Test
    @DisplayName("when the connection closed")
    @Order(4)
    void testAfterConnectionClosed() throws Exception {
        when(mockSession.getAttributes()).thenReturn(Map.of("userId","testUser"));
        handlers.afterConnectionClosed(mockSession,closeStatus);
        assertThrows(UserNotFound.class,()->handlers.getSessionByUserId("testUser"));
    }


}