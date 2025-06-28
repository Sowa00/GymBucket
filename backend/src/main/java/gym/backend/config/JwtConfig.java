package gym.backend.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    @Bean
    public SecretKey jwtSecretKey() {
        // Ensure the secret is at least 512 bits (64 bytes) for HS512
        if (jwtSecret.length() < 64) {
            throw new IllegalArgumentException("JWT secret must be at least 64 characters long for HS512");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public long getJwtRefreshExpirationMs() {
        return jwtRefreshExpirationMs;
    }
}