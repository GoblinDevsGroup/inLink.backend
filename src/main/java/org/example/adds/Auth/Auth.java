package org.example.adds.Auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.adds.Security.JwtUtil;
import org.example.adds.Users.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class Auth {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(usersService.saveDraftUser(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPhone(@Valid @RequestBody SigningRequest request) {
        return ResponseEntity.ok(usersService.verifyPhone(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new Token(accessToken));
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Phone phone) {
            return ResponseEntity.ok(usersService.forgotPassword(phone.phone()));
    }

    @PatchMapping("/forgot/verify")
    public ResponseEntity<?> verifyForgotPassword(@Valid @RequestBody VerifyForgotPassword dto){
        return ResponseEntity.ok(usersService.activatePhone(dto));
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody SetNewPassword dto){
        return ResponseEntity.ok(usersService.setNewPassword(dto));
    }

    @Data
    static class Token {
        private String accessToken;

        public Token(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
