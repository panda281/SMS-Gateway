package com.meinab.smsgateway.configs;


import com.meinab.smsgateway.handlers.WebSocketChannelInterceptor;
import com.meinab.smsgateway.handlers.WebSocketHandlers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketChannelInterceptor webSocketChannelInterceptor;
    private final WebSocketHandlers webSocketHandlers;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandlers, "/sms-ws").setAllowedOrigins("*")
                .addInterceptors(webSocketChannelInterceptor)
                .setHandshakeHandler(new DefaultHandshakeHandler());
    }
}
