package com.service.userservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret ;//= "0f9917fa41061d74cd86e4515811dac619a8cdd0d640a0e4c70781170bf6b17d";

    @Value("${jwt.expiration}")
    private long expiration ;//= 604800L;

    public String generateToken(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        System.out.println("UserName = "+username);
        return Jwts.builder()
                //.setClaims(Collections.emptyMap())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUserName(String token) {
        try {
            JwtParser jwtParser = Jwts.parser().setSigningKey(secret).build();
            String username=jwtParser.parseClaimsJws(token).getBody().getSubject();
            return username;
            //Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            //Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validataToken(String token, UserDetails userDetails) {
        String username =getUserName(token);
        return (username.equals(userDetails.getUsername()));
    }
}
