package com.meinab.smsgateway.handlers;

import com.meinab.smsgateway.exceptions.UserNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

    private void setupTestSession(String userId) throws Exception {
        when(mockSession.getAttributes()).thenReturn(Map.of("userId", userId));
        handlers.afterConnectionEstablished(mockSession);
    }

    @Test
    @DisplayName("when the connection established")
    void testAfterConnectionEstablished_SendWelcomeMessage() throws Exception {
        setupTestSession("testUser");

        verify(mockSession, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("Retrieves user ID from session attributes")
    void testAfterConnectionEstablished_RetrievesUserId() throws Exception {
        setupTestSession("testUser");

        WebSocketSession retrievedUserId = handlers.getSession((String) mockSession.getAttributes().get("userId"));
        assertEquals("testUser", retrievedUserId.getAttributes().get("userId"));
    }

    @Test
    @DisplayName("Stores session in map by user ID")
    void testAfterConnectionEstablished_StoresSessionByUserId() throws Exception {
        setupTestSession("testUser");

        assertEquals(mockSession, handlers.getSessionByUserId("testUser"));
    }

    @Test
    @DisplayName("get session by id for existing user")
    void testGetSessionByUserIdForTheExistingUser() throws Exception {
        setupTestSession("testUser");

        assertDoesNotThrow(() -> handlers.getSessionByUserId("testUser"));
    }

    @Test
    @DisplayName("get session by id for non-existing user")
    void testGetSessionByUserIdWhenForNewUser() {
        assertThrows(UserNotFound.class, () -> handlers.getSessionByUserId("nonExistingUser"));
    }

    @Test
    @DisplayName("when the connection closed")
    void testAfterConnectionClosed() throws Exception {
        setupTestSession("testUser");

        handlers.afterConnectionClosed(mockSession, closeStatus);
        assertThrows(UserNotFound.class, () -> handlers.getSessionByUserId("testUser"), "Session for the user still exists and it is not closed");
    }


}