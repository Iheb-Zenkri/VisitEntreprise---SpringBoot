package Spring.Visit.SharedModule.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        logger.info("Configuring WebSocket message broker...");

        config.enableSimpleBroker("/topic", "queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");

        logger.info("WebSocket message broker configured successfully.");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("Registering WebSocket STOMP endpoints...");

        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();

        logger.info("WebSocket STOMP endpoints registered successfully.");
    }
}
