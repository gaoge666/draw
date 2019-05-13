package com.server.draw.websocket;

import com.server.draw.websocket.handshake.StompMessageHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个STOMP的endpoint,并指定使用SockJS协议
//        registry.addEndpoint("websocket")
//                .setHandshakeHandler(new StompMessageHandshakeHandler())
//                //.addInterceptors(new WebSocketHandshakeInterceptor())
//                .setAllowedOrigins("*")
//                .withSockJS();
        registry.addEndpoint("webSocket")
                .setHandshakeHandler(new StompMessageHandshakeHandler())
                .setAllowedOrigins("*");
        registry.addEndpoint("canvas")
                .setHandshakeHandler(new StompMessageHandshakeHandler())
                .setAllowedOrigins("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(Message Broker)
        //广播式应配置一个消息代理,名为topic,前缀为app 访问方式为 address:port//app/topic
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

    }

}
