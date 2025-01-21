package org.example.adds.Auth;

import lombok.Data;

@Data
public class SigningRequest {
    private String phone;
    private String smsCode;
}
