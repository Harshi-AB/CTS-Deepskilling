import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the JWT payload (claims set): the actual data being
 * securely communicated between parties - standard registered claims
 * (iss, sub, aud, exp, iat) plus any custom application claims.
 */
public class JwtPayload {

    private final Map<String, Object> claims = new LinkedHashMap<>();

    public JwtPayload setIssuer(String issuer) {
        claims.put("iss", issuer);
        return this;
    }

    public JwtPayload setSubject(String subject) {
        claims.put("sub", subject);
        return this;
    }

    public JwtPayload setAudience(String audience) {
        claims.put("aud", audience);
        return this;
    }

    public JwtPayload setIssuedAt(long epochSeconds) {
        claims.put("iat", epochSeconds);
        return this;
    }

    public JwtPayload setExpiration(long epochSeconds) {
        claims.put("exp", epochSeconds);
        return this;
    }

    public JwtPayload addClaim(String name, Object value) {
        claims.put(name, value);
        return this;
    }

    public Object getClaim(String name) {
        return claims.get(name);
    }

    public boolean isExpired() {
        Object exp = claims.get("exp");
        if (exp == null) {
            return false;
        }
        long expEpoch = Long.parseLong(String.valueOf(exp));
        return System.currentTimeMillis() / 1000 > expEpoch;
    }

    public Map<String, Object> toMap() {
        return new LinkedHashMap<>(claims);
    }

    public static JwtPayload fromMap(Map<String, Object> map) {
        JwtPayload payload = new JwtPayload();
        payload.claims.putAll(map);
        return payload;
    }
}
