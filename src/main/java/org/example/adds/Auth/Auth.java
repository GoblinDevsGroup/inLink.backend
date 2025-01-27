package org.example.adds.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.adds.Response;
import org.example.adds.Security.JwtUtil;
import org.example.adds.Users.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<Response> signup(@Valid @RequestBody SignUpRequest request) {
            String response = usersService.saveDraftUser(request);
            return ResponseEntity.ok(new Response(response,true));
    }

    @PostMapping("/verify")
    @Operation(responses = {@ApiResponse()})
    public ResponseEntity<Response> verifyPhone(@Valid @RequestBody SigningRequest request) {
            String response = usersService.verifyPhone(request);
            return ResponseEntity.ok(new Response(response, true));
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@Valid @RequestBody LoginRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new Token(accessToken, "Login successfully", true));
        }
        catch (Exception e){
            return ResponseEntity.status(400).body(new Token(null, e.getMessage(), false));
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<Response> forgotPassword(@RequestBody Phone phone) {
        try {
            String response = usersService.forgotPassword(phone.phone());
            return ResponseEntity.ok(new Response(response, true));
        }catch (RuntimeException ex){
            return ResponseEntity.ok(new Response(ex.getMessage(), false));
        }
    }

    @PatchMapping("/forgot/verify")
    public ResponseEntity<Response> verifyForgotPassword(@Valid @RequestBody VerifyForgotPassword dto){
        String response = usersService.activatePhone(dto);
        return ResponseEntity.ok(new Response(response, true));
    }

    @PatchMapping("/reset")
    public ResponseEntity<Users> resetPassword(@RequestBody SetNewPassword dto){
        return ResponseEntity.ok(usersService.setNewPassword(dto));
    }

    @DeleteMapping("delete/{phone}")
    public ResponseEntity<String> delete(@PathVariable String phone){
        return ResponseEntity.ok(usersService.delete(phone));
    }

    @Data
    public static class Token {
        private String accessToken;
        private String message;
        private boolean success;

        public Token(String accessToken, String message, boolean success) {
            this.accessToken = accessToken;
            this.message = message;
            this.success = success;
        }
    }
}
