package nl.fontys.s3.dinemasterbackend.configuration;

import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessTokenDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final AccessTokenDecoder accessTokenDecoder;
    private final String[] allowedOrigins;

    public WebSocketConfiguration(AccessTokenDecoder accessTokenDecoder,
                                  @Value("${cors.allowedOrigins:}") String[] allowedOrigins) {
        this.accessTokenDecoder = accessTokenDecoder;
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowedOrigins)
                .addInterceptors(new TokenHandshakeInterceptor(accessTokenDecoder));
    }


}
