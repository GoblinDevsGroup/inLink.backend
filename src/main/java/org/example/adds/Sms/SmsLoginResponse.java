package org.example.adds.Sms;

public record SmsLoginResponse(
        Data data,
        String message,
        String tokenType
) {
}
