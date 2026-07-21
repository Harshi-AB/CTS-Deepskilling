# Exercise 3: Using JSON Web Tokens (JWT) for Secure Communication

## Compile
javac *.java

## Run
java JWTSecureCommunicationDemo

## Classes
- Base64UrlCodec.java     - Base64URL encode/decode helper
- JsonUtil.java            - minimal flat-JSON reader/writer (Core Java only)
- JwtHeader.java            - JWT header (alg, typ)
- JwtPayload.java           - JWT claims set
- JwtToken.java             - immutable holder for an encoded token
- JwtUtil.java              - HMAC-SHA256 signing and verification engine
- JWTSecureCommunicationDemo.java - main class, runs valid + attack scenarios
