package nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.*;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.exception.InvalidAccessTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RefreshTokenEncoderDecoderImpl implements RefreshTokenEncoder, RefreshTokenDecoder {
    private final Key key;

    public RefreshTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String encode(RefreshToken refreshToken) {
        Map<String, Object> claimsMap = new HashMap<>();
        if (refreshToken.getUserId() != null) {
            claimsMap.put("userId", refreshToken.getUserId());
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(refreshToken.getSubject())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(60, ChronoUnit.MINUTES)))
                .addClaims(claimsMap)
                .signWith(key)
                .compact();
    }

    @Override
    public RefreshToken decode(String refreshTokenEncoded) {
        try {
            Jwt<?, Claims> jwt = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(refreshTokenEncoded);
            Claims claims = jwt.getBody();

            Long userId = claims.get("userId", Long.class);

            return new RefreshTokenImpl(claims.getSubject(), userId);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}
