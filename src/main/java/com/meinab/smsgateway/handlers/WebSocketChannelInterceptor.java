package com.meinab.smsgateway.handlers;


import com.meinab.smsgateway.services.AuthenticationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements HandshakeInterceptor {
    private final AuthenticationService authenticationService;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpHeaders headers = servletRequest.getHeaders();

            String token = headers.getFirst("Authorization");
            log.info(token);
            if (token != null) {
                String result = authenticationService.login(token);
                if (result!=null) {
                    attributes.put("userId", result);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {
        log.info("handshake done");
    }


}
