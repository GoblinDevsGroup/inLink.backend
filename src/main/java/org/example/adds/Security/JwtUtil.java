package org.example.adds.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.adds.Auth.Dto.TokenResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private final UserDetailsService userDetailsService;
    private final UsersRepo usersRepo;

    public JwtUtil(UserDetailsService userDetailsService, UsersRepo usersRepo) {
        this.userDetailsService = userDetailsService;
        this.usersRepo = usersRepo;
    }


    public TokenResponse generateToken(UserDetails userDetails) {
        // Fetch userId from your user service or repository
        Users user = usersRepo.findByPhone(userDetails.getUsername())
                .orElseThrow(()->new NoSuchElementException("error in fetching user data"));
        UUID userId = user.getId();

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))  // 24 hours
                .claim("role", roles)
                .claim("fullName", user.getFullName())
                .claim("companyName", user.getCompanyName())
                .claim("userId", userId)
                .signWith(getKey())
                .compact();
        return new TokenResponse(accessToken, user.getFullName());
    }


    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Key getKey() {
        byte[] bytes = Decoders.BASE64.decode("1234567891234567891234567891234567891234567891234567891234567890");
        return Keys.hmacShaKeyFor(bytes);
    }

    public Authentication getUser(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String roles = claims.get("role", String.class);
        List<SimpleGrantedAuthority> authorities = roles != null
                ? Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : List.of();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
