import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * JwtDemoClient
 * ----------------------------------------------------------------------
 * Demonstrates the FULL secure-communication flow against AuthServer,
 * end to end, without needing curl or Postman:
 *
 *   1) Calls POST /login with valid credentials -> receives a JWT.
 *   2) Uses that JWT to call the PROTECTED GET /secure/profile endpoint.
 *   3) Then deliberately calls /secure/profile WITHOUT a token, and with
 *      a tampered token, to show both are correctly rejected.
 *
 * IMPORTANT: Start AuthServer.java FIRST, then run this class.
 * ----------------------------------------------------------------------
 */
public class JwtDemoClient {

    private static final String BASE_URL = "http://localhost:9000";

    public static void main(String[] args) throws IOException {
        System.out.println("=== Step 1: Logging in as 'alice' ===");
        String loginResponse = callPost(BASE_URL + "/login?username=alice&password=alice123");
        System.out.println("Login response: " + loginResponse);

        String token = extractToken(loginResponse);
        System.out.println("\nExtracted JWT:\n" + token);

        System.out.println("\n=== Step 2: Calling the PROTECTED endpoint WITH a valid token ===");
        String secureResponse = callGetWithAuth(BASE_URL + "/secure/profile", token);
        System.out.println("Secure response: " + secureResponse);

        System.out.println("\n=== Step 3: Calling the PROTECTED endpoint with NO token ===");
        String noAuthResponse = callGetWithAuth(BASE_URL + "/secure/profile", null);
        System.out.println("Response (should be 401 Unauthorized): " + noAuthResponse);

        System.out.println("\n=== Step 4: Calling the PROTECTED endpoint with a TAMPERED token ===");
        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";
        String tamperedResponse = callGetWithAuth(BASE_URL + "/secure/profile", tamperedToken);
        System.out.println("Response (should be 401 Unauthorized): " + tamperedResponse);
    }

    private static String callPost(String endpoint) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (var os = connection.getOutputStream()) {
            os.write(new byte[0]);
        }
        return readBody(connection);
    }

    private static String callGetWithAuth(String endpoint, String token) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }
        return readBody(connection);
    }

    private static String readBody(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        InputStreamReader streamReader = (status >= 200 && status < 300)
                ? new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
                : new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(streamReader)) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            return "[HTTP " + status + "] " + body;
        }
    }

    /** Pulls the "token" field out of a flat JSON response like {"token":"..."}. */
    private static String extractToken(String json) {
        String marker = "\"token\":\"";
        int start = json.indexOf(marker) + marker.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
