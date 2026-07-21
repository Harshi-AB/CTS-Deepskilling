# Exercise 1: Implementing Centralized Authentication with OAuth 2.1 / OIDC

## Compile
javac *.java

## Run
java CentralizedAuthenticationDemo

## Classes
- User.java              - resource owner (end user) with identity claims
- Client.java             - registered relying-party application
- AuthorizationCode.java  - single-use, PKCE-bound authorization code
- AccessToken.java        - OAuth access token
- IDToken.java            - OIDC identity token
- AuthorizationServer.java- central server: authenticates users, issues codes/tokens
- CentralizedAuthenticationDemo.java - main class, runs the full flow + attack scenarios
