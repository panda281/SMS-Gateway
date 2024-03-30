package com.meinab.smsgateway.handlers;


import com.meinab.smsgateway.exceptions.UserNotFound;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandlers implements WebSocketHandler {
    private static final Map<String, WebSocketSession> sessions= new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        sessions.put(userId,session);
        log.info("Welcome {}",userId);
        session.sendMessage(new TextMessage("Welcome " + session.getId()));
    }
    WebSocketSession getSession(String sessionId) {
        return WebSocketHandlers.sessions.get(sessionId);
    }

    public WebSocketSession getSessionByUserId(String userId) {
        WebSocketSession session = sessions.get(userId);
        if (session == null) {
            throw new UserNotFound("Session for the user not found");
        }
        return session;
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            log.info(message.toString());
    }

    public void sendMessage(String username, String messages) throws IOException {
        WebSocketSession session = getSessionByUserId(username);
        if(!session.isOpen())
            throw new UserNotFound("session is not open ");
        session.sendMessage(new TextMessage(messages));
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session,@NotNull Throwable exception) throws Exception {
        log.error(exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);
        log.info(String.valueOf(sessions.size()));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
