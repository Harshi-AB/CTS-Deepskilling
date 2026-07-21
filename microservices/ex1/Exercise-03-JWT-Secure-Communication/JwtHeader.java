import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the JWT header segment - metadata about the token itself,
 * primarily the signing algorithm ("alg") and token type ("typ").
 */
public class JwtHeader {

    private final String algorithm;
    private final String type;

    public JwtHeader(String algorithm, String type) {
        this.algorithm = algorithm;
        this.type = type;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("alg", algorithm);
        map.put("typ", type);
        return map;
    }

    public static JwtHeader fromMap(Map<String, Object> map) {
        return new JwtHeader(String.valueOf(map.get("alg")), String.valueOf(map.get("typ")));
    }
}
