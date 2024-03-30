package com.meinab.smsgateway.services;

import com.meinab.smsgateway.exceptions.UserNotFound;
import com.meinab.smsgateway.handlers.WebSocketHandlers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSender {
    private final WebSocketHandlers webSocketHandlers;

    public void sendMessage(String username, String message) throws IOException {
        WebSocketSession session = webSocketHandlers.getSessionByUserId(username);
        if (!session.isOpen()) {
            throw new UserNotFound("Session for the user not found");
        }
        session.sendMessage(new TextMessage(message));
    }
}
