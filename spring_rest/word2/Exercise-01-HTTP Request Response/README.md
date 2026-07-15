# Exercise 01 - HTTP Request Response

Plain Core Java demo (no Spring, no Maven needed) that opens a raw TCP
socket and hand-writes an HTTP/1.1 request, then prints the raw status
line and headers that come back — illustrating the request/response
structure described in RFC 7230.

## Compile
```
javac *.java
```

## Run
```
java HttpRequestResponseDemo
```

## Expected Output (with internet access to example.com)
```
===== Sending raw HTTP request to example.com =====

---- Request sent ----
GET / HTTP/1.1
Host: example.com
User-Agent: CoreJavaHttpDemo/1.0
Accept-Language: en
Connection: close

---- Response received ----
Status Line -> HTTP/1.1 200 OK
Header      -> Content-Type: text/html; charset=UTF-8
Header      -> ... (more headers)
(blank line - headers end here, body follows)

(Only the status line and headers are printed above;
 the response body follows after the blank line, per RFC 7230.)
```

If there is no network access, the program falls back to printing the
sample request/response from RFC 7230 so the structure is still visible.
