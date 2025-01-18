package nl.fontys.s3.dinemasterbackend.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessTokenDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
public class TokenHandshakeInterceptor implements HandshakeInterceptor {
    private final AccessTokenDecoder accessTokenDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = null;

        //This is for params
        if (request.getURI().getQuery() != null) {
            // If using query parameters
            String[] queryParams = request.getURI().getQuery().split("&");
            for (String param : queryParams) {
                if (param.startsWith("token=")) {
                    token = param.substring(6);
                    break;
                }
            }
        }
        //This is for headers
        else if (request.getHeaders().containsKey("Authorization")) {
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token != null) {
            //Transform the JWT token to Authentication (spring framework object that can be stored in the context)
            Authentication authentication = getAuthenticationFromToken(token);
            //We should set the context so that the authentication of the user can be used throughout the websocket session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            attributes.put("principal", authentication.getPrincipal());
            return true;
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    public Authentication getAuthenticationFromToken(String token) {
        try {
            // Decode the token
            AccessToken decodedToken = accessTokenDecoder.decode(token);

            // Extract user info and roles from AccessToken
            String username = decodedToken.getSubject();
            Set<String> roles = decodedToken.getRoles();

            // Convert roles to GrantedAuthority objects
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create an Authentication object
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (Exception e) {
            // If there's any error in decoding or authentication
            return null;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
    }

}
