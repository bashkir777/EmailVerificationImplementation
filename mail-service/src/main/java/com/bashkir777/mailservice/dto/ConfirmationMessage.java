package com.bashkir777.mailservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationMessage {
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("otp")
    private String otp;
}
