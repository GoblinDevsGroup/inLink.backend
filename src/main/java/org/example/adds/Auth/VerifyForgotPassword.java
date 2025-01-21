package org.example.adds.Auth;

public record VerifyForgotPassword(
        String phone,
        String smsCode
) {
}
