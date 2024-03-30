package com.meinab.smsgateway.handlers;

import com.meinab.smsgateway.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketChannelInterceptorTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ServletServerHttpRequest mockRequest;
    @Mock
    private WebSocketHandlers mockWebSocketHandlers;
    @Mock
    private ServletServerHttpResponse mockResponse;
    @Mock
    private WebSocketHandler mockWsHandler;
    @InjectMocks
    private WebSocketChannelInterceptor webSocketChannelInterceptor;
    private HttpHeaders headers;
    private Map<String, Object> attributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
        attributes = new HashMap<>();
        when(mockRequest.getHeaders()).thenReturn(headers);
    }

    @Test
    @DisplayName("when the token is valid")
    void testBeforeHandshakeWithValidToken() throws Exception {
        // Arrange
        headers.add("Authorization", "validToken");
        when(authenticationService.login("validToken")).thenReturn("userId");

        // Act
        boolean handshake = webSocketChannelInterceptor.beforeHandshake(mockRequest, mockResponse, mockWsHandler, attributes);

        // Assert
        assertTrue(handshake);
        assertEquals("userId", attributes.get("userId"));
    }

    @Test
    @DisplayName("when the token is invalid")
    void testBeforeHandshakeWithInvalidToken() {
        // Arrange
        headers.add("Authorization", "invalidToken");
        when(authenticationService.login("invalidToken")).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> webSocketChannelInterceptor.beforeHandshake(mockRequest, mockResponse, mockWsHandler, attributes));
    }

    @Test
    @DisplayName("when the user connected to multiple devices")
    void testBeforeHandshakeWithForbiddenUser() throws Exception {
        // Arrange
        headers.add("Authorization", "validToken");
        when(authenticationService.login("validToken")).thenReturn("userId");

        // Act
        when(mockWebSocketHandlers.getSession("userId")).thenReturn(mock(WebSocketSession.class));
        boolean handshake = webSocketChannelInterceptor.beforeHandshake(mockRequest, mockResponse, mockWsHandler, attributes);

        // Assert
        assertFalse(handshake);
        verify(mockResponse).setStatusCode(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("when there is no token passed")
    void testBeforeHandshakeWithNoToken() throws Exception {
        // Act
        boolean handshake = webSocketChannelInterceptor.beforeHandshake(mockRequest, mockResponse, mockWsHandler, attributes);

        // Assert
        assertFalse(handshake);
    }
}