package com.meinab.smsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageResponseModel {
    private String phoneNo;
    private boolean isSentSuccessfully;
}
