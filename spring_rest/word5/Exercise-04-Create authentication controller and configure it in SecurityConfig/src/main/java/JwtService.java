import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Authentication service responsible for generating and validating
 * JSON Web Tokens (JWT).
 *
 * A JWT is composed of a header, a payload (claims) and a signature.
 * This service signs tokens with an HMAC-SHA256 secret key so that any
 * tampering with the payload can be detected when the token is later
 * validated (see {@link #validateToken(String, String)}).
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign tokens. In a real production system this
     * would be injected from a secure vault / environment variable
     * rather than hard-coded, but a fixed key keeps this exercise
     * self-contained and reproducible.
     */
    private static final String SECRET_KEY =
            "CognizantDigitalNurture5SpringRestJwtExerciseSecretKeyMustBeLongEnough";

    /** Token validity period: 1 hour, expressed in milliseconds. */
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Generates a signed JWT for the given username with no extra claims.
     */
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    /**
     * Generates a signed JWT for the given username, embedding any extra
     * claims supplied by the caller (e.g. roles).
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extracts the username (the "subject" claim) from a token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extracts the expiration date from a token. */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Generic helper to pull any single claim out of the token. */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Parses the token and returns all of its claims, verifying the signature. */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Returns true if the token has expired. */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates a token against an expected username: the signature must
     * be valid, the username must match and the token must not be expired.
     */
    public boolean validateToken(String token, String expectedUsername) {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }
}
