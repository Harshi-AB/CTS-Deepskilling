import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * HttpRequestResponseDemo
 *
 * Demonstrates the raw structure of an HTTP request and an HTTP response,
 * as described in RFC 7230 (https://tools.ietf.org/html/rfc7230).
 *
 * This program opens a plain TCP socket to a web server (no HTTP library
 * is used) and writes a hand-built HTTP/1.1 GET request. It then reads
 * and prints the raw bytes that come back from the server so you can see
 * exactly what an HTTP request and an HTTP response look like on the wire:
 *
 *   Request  -> Method + Resource + Version, followed by headers
 *   Response -> Version + Status Code + Reason, followed by headers and body
 *
 * Core Java only. No package declaration. Compiles with: javac *.java
 * Runs with: java HttpRequestResponseDemo
 */
public class HttpRequestResponseDemo {

    // Target host used for the demonstration
    private static final String HOST = "example.com";
    private static final int PORT = 80;

    public static void main(String[] args) {
        System.out.println("===== Sending raw HTTP request to " + HOST + " =====\n");

        try (Socket socket = new Socket(HOST, PORT)) {

            // ---------- Build and send the HTTP REQUEST ----------
            // Line 1: Method, Resource (URL path), HTTP Version
            // Line 2+: Request headers (client info, host, etc.)
            String request =
                    "GET / HTTP/1.1\r\n" +          // Method type, Resource, HTTP Version
                    "Host: " + HOST + "\r\n" +       // Server that will respond to this request
                    "User-Agent: CoreJavaHttpDemo/1.0\r\n" +
                    "Accept-Language: en\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";                          // blank line marks end of headers

            System.out.println("---- Request sent ----");
            System.out.print(request);

            OutputStream out = socket.getOutputStream();
            out.write(request.getBytes("UTF-8"));
            out.flush();

            // ---------- Read the HTTP RESPONSE ----------
            // Line 1: HTTP Version, Response Status code, Response Message
            // Following lines: Response headers, then a blank line, then the body
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("---- Response received ----");
            String line;
            boolean firstLine = true;
            int linesPrinted = 0;

            while ((line = in.readLine()) != null && linesPrinted < 25) {
                if (firstLine) {
                    System.out.println("Status Line -> " + line);
                    firstLine = false;
                } else if (line.isEmpty()) {
                    System.out.println("(blank line - headers end here, body follows)");
                } else {
                    System.out.println("Header      -> " + line);
                }
                linesPrinted++;
            }

            System.out.println("\n(Only the status line and headers are printed above;");
            System.out.println(" the response body follows after the blank line, per RFC 7230.)");

        } catch (Exception e) {
            System.out.println("Could not reach " + HOST + " (no network access in this environment).");
            System.out.println("Below is a sample HTTP request/response captured from RFC 7230 instead:\n");
            printSampleFromRfc();
        }
    }

    /**
     * Prints the exact sample request/response referenced in the hands-on
     * document (RFC 7230), so the structure is always visible even without
     * network access.
     */
    private static void printSampleFromRfc() {
        System.out.println("---- Sample Request ----");
        System.out.println("GET /hello.txt HTTP/1.1");
        System.out.println("User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3");
        System.out.println("Host: www.example.com");
        System.out.println("Accept-Language: en, mi");

        System.out.println("\n---- Sample Response ----");
        System.out.println("HTTP/1.1 200 OK");
        System.out.println("Date: Mon, 27 Jul 2009 12:28:53 GMT");
        System.out.println("Server: Apache");
        System.out.println("Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT");
        System.out.println("ETag: \"34aa387-d-1568eb00\"");
        System.out.println("Accept-Ranges: bytes");
        System.out.println("Content-Length: 51");
        System.out.println("Vary: Accept-Encoding");
        System.out.println("Content-Type: text/plain");
        System.out.println();
        System.out.println("Hello World! My payload includes a trailing CRLF.");
    }
}
