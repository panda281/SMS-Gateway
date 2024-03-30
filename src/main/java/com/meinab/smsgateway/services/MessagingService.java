package com.meinab.smsgateway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meinab.smsgateway.dto.MessageResponseModel;
import com.meinab.smsgateway.dto.MessagingDto;
import com.meinab.smsgateway.handlers.WebSocketHandlers;
import com.meinab.smsgateway.models.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final ObjectMapper objectMapper;
    private final MessageSender messageSender;
    public MessageResponseModel sendMessage(MessagingDto messagingDto) throws IOException {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> messageData = new HashMap<>();
        messageData.put("to", messagingDto.getPhoneNo());
        messageData.put("message", messagingDto.getMessage());
        String jsonMessage = objectMapper.writeValueAsString(messageData);
        messageSender.sendMessage(users.getUsername(), jsonMessage);
        return MessageResponseModel.builder().phoneNo(messagingDto.getPhoneNo()).isSentSuccessfully(true).build();
    }
}
