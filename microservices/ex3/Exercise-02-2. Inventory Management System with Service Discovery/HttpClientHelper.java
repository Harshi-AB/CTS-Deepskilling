import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * HttpClientHelper is a small, dependency-free REST client used by
 * services in this project to call one another over HTTP, once the
 * target address has been resolved through the ServiceRegistry
 * (the stand-in for Eureka service discovery).
 */
public final class HttpClientHelper {

    private HttpClientHelper() {
        // Utility class
    }

    public static String get(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            int status = connection.getResponseCode();
            InputStream stream = (status >= 200 && status < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            return readStream(stream);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String post(String url, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            InputStream stream = (status >= 200 && status < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            return readStream(stream);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /** Performs a PUT request with a JSON body and returns the response body. */
    public static String put(String url, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            InputStream stream = (status >= 200 && status < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            return readStream(stream);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String readStream(InputStream stream) throws Exception {
        if (stream == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
