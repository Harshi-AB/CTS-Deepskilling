import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The heart of Exercise 06.
 *
 * This filter runs once per incoming request, BEFORE Spring Security's
 * normal authentication filters. Its job:
 *   1. Read the "Authorization" header.
 *   2. If it starts with "Bearer ", strip the prefix to get the raw JWT.
 *   3. Validate the token's signature and expiry via {@link JwtService}.
 *   4. If valid, extract the username and the "roles" claim, build a
 *      fully-authenticated {@link UsernamePasswordAuthenticationToken},
 *      and place it into the SecurityContext.
 *   5. Downstream, SecurityConfig's authorizeHttpRequests / hasRole
 *      rules can then make their decision purely from this context -
 *      completing "authorization based on JWT".
 *
 * If the header is missing or invalid, the filter simply does nothing
 * and lets the request continue unauthenticated; SecurityConfig will
 * then reject it with 401/403 if the target endpoint requires auth.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // No Bearer token present -> let the request pass through
        // unauthenticated; SecurityConfig decides whether that's allowed.
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            String username = jwtService.extractUsername(jwt);

            // Only (re)authenticate if nothing is already authenticated
            // for this request in the SecurityContext.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (!jwtService.isTokenExpired(jwt)) {
                    List<GrantedAuthority> authorities = extractAuthorities(jwt);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception ex) {
            // Any parsing/signature/expiry problem -> treat as unauthenticated.
            // We deliberately do not throw here; SecurityConfig's access
            // rules will produce the appropriate 401/403 response.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Reads the comma-separated "roles" claim embedded in the token by
     * AuthController (e.g. "ROLE_USER,ROLE_ADMIN") and converts it into
     * Spring Security {@link GrantedAuthority} objects.
     */
    private List<GrantedAuthority> extractAuthorities(String jwt) {
        String rolesClaim = jwtService.extractClaim(jwt, claims -> claims.get("roles", String.class));

        if (rolesClaim == null || rolesClaim.isBlank()) {
            return List.of();
        }

        return Arrays.stream(rolesClaim.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
