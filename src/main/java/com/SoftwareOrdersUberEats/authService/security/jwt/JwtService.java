package com.SoftwareOrdersUberEats.authService.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.security.Key;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(String username, List<String> roles){
        return Jwts.builder()
                .signWith(getSecret(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + 60000 * 300)) //  hours
                .setSubject(username)
                .compact();
    }

    public Key getSecret(){
        byte[] keys = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keys);
    }
}
