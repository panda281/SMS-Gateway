package com.meinab.smsgateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessagingDto {
    @NotBlank
    @Pattern(regexp = "^\\+251[79]\\d{8}$", message = "must match +251 + 7 / 9 + * * * * * * * *")
    private String phoneNo;
    @NotBlank
    private String message;
}
