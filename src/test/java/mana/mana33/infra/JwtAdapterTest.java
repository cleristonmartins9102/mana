package mana.mana33.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mana.mana33.domain.models.TokenPayloadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtAdapterTest {

    private JwtAdapter jwtAdapter;
    private String secret;
    private long expirationTime;

    @BeforeEach
    void setUp() {
        secret = "my-super-secret-key-with-at-least-32-characters-for-hs256";
        expirationTime = 3600000L; // 1 hour in milliseconds
        jwtAdapter = new JwtAdapter(secret, expirationTime);
    }

    @Test
    void shouldGenerateValidJwtToken() {
        TokenPayloadDTO payload = createTestPayload();

        String token = jwtAdapter.encrypt(payload);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts: header.payload.signature
    }

    @Test
    void shouldIncludeAllPayloadClaimsInToken() {
        TokenPayloadDTO payload = createTestPayload();
        payload.id = "123";

        String token = jwtAdapter.encrypt(payload);

        Claims claims = decodeToken(token);

        assertEquals("123", claims.get("id"));
        assertEquals("John", claims.get("firstName"));
        assertEquals("Doe", claims.get("lastName"));
        assertEquals("john.doe@example.com", claims.get("email"));
    }

    @Test
    void shouldSetIssuedAtDate() {
        TokenPayloadDTO payload = createTestPayload();
        long beforeEncrypt = System.currentTimeMillis();

        String token = jwtAdapter.encrypt(payload);

        long afterEncrypt = System.currentTimeMillis();
        Claims claims = decodeToken(token);

        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getIssuedAt().getTime() >= beforeEncrypt);
        assertTrue(claims.getIssuedAt().getTime() <= afterEncrypt);
    }

    @Test
    void shouldSetExpirationDateCorrectly() {
        TokenPayloadDTO payload = createTestPayload();
        long beforeEncrypt = System.currentTimeMillis();

        String token = jwtAdapter.encrypt(payload);

        long afterEncrypt = System.currentTimeMillis();
        Claims claims = decodeToken(token);

        assertNotNull(claims.getExpiration());
        long expectedMinExpiration = beforeEncrypt + expirationTime;
        long expectedMaxExpiration = afterEncrypt + expirationTime;

        assertTrue(claims.getExpiration().getTime() >= expectedMinExpiration);
        assertTrue(claims.getExpiration().getTime() <= expectedMaxExpiration);
    }

    @Test
    void shouldGenerateDifferentTokensForSamePayloadAtDifferentTimes() throws InterruptedException {
        TokenPayloadDTO payload = createTestPayload();

        String token1 = jwtAdapter.encrypt(payload);
        Thread.sleep(10); // Small delay to ensure different timestamps
        String token2 = jwtAdapter.encrypt(payload);

        assertNotEquals(token1, token2);
    }

    @Test
    void shouldHandlePayloadWithNullId() {
        TokenPayloadDTO payload = createTestPayload();
        payload.id = null;

        String token = jwtAdapter.encrypt(payload);

        assertNotNull(token);
        Claims claims = decodeToken(token);
        assertNull(claims.get("id"));
    }

    @Test
    void shouldBeVerifiableWithSameSecret() {
        TokenPayloadDTO payload = createTestPayload();

        String token = jwtAdapter.encrypt(payload);

        assertDoesNotThrow(() -> {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        });
    }

    @Test
    void shouldNotBeVerifiableWithDifferentSecret() {
        TokenPayloadDTO payload = createTestPayload();
        String token = jwtAdapter.encrypt(payload);

        String differentSecret = "different-secret-key-with-at-least-32-characters-for-hs256";
        SecretKey differentKey = Keys.hmacShaKeyFor(differentSecret.getBytes(StandardCharsets.UTF_8));

        assertThrows(Exception.class, () -> {
            Jwts.parser()
                    .verifyWith(differentKey)
                    .build()
                    .parseSignedClaims(token);
        });
    }

    @Test
    void shouldDecryptTokenAndReturnUserId() {
        TokenPayloadDTO payload = createTestPayload();
        payload.id = "user-123";

        String token = jwtAdapter.encrypt(payload);
        String decryptedId = jwtAdapter.decrypt(token);

        assertEquals("user-123", decryptedId);
    }

    private TokenPayloadDTO createTestPayload() {
        TokenPayloadDTO payload = new TokenPayloadDTO();
        payload.firstName = "John";
        payload.lastName = "Doe";
        payload.email = "john.doe@example.com";
        return payload;
    }

    private Claims decodeToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
