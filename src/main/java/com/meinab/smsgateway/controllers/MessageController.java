package com.meinab.smsgateway.controllers;

import com.meinab.smsgateway.dto.MessageResponseModel;
import com.meinab.smsgateway.dto.MessagingDto;
import com.meinab.smsgateway.services.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageController {
    private final MessagingService messagingService;

    @PostMapping("/sms")
    public ResponseEntity<MessageResponseModel> sendMessage(@Valid @RequestBody MessagingDto messagingDto) throws IOException {
        return ResponseEntity.ok(messagingService.sendMessage(messagingDto));
    }

}
