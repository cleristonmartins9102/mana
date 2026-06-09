package mana.mana33.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mana.mana33.domain.Decrypter;
import mana.mana33.domain.Encrypt;
import mana.mana33.domain.models.TokenPayloadDTO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtAdapter implements Encrypt, Decrypter {
    private final SecretKey key;
    private final long expirationTime;

    public JwtAdapter(String secret, long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public String encrypt(TokenPayloadDTO payload) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationTime);

        return Jwts.builder()
                .claim("id", payload.getId())
                .claim("firstName", payload.getFirstName())
                .claim("lastName", payload.getLastName())
                .claim("email", payload.getEmail())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    @Override
    public String decrypt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("id", String.class);
    }
}
