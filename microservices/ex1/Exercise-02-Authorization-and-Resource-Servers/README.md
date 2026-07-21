# Exercise 2: Configuring Authorization Servers and Resource Servers

## Compile
javac *.java

## Run
java AuthorizationResourceServerDemo

## Classes
- Scope.java                - enum of OAuth scopes
- AccessToken.java           - scoped, expirable, revocable access token
- RegisteredClient.java      - client configuration (allowed scopes)
- TokenStore.java            - shared trust boundary between AS and RS
- AuthorizationServer.java   - issues scoped tokens via client-credentials grant
- ProtectedResource.java     - a resource guarded by a required scope
- ResourceServer.java        - validates tokens and enforces scopes per request
- AuthorizationResourceServerDemo.java - main class, runs several access scenarios
