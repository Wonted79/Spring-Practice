package com.example.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //STOMP 기반 WebSocket 메시지 브로커 기능을 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { //WebSocket 설정 매서드 인터페이스

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*메모리 기반 메시지 브로커 활성화  /sub 경로 구독 가능
        클라이언트가 /sub/chat/room/1 을 구독
        → 이 경로로 메시지가 오면 자동으로 받음
         */
        config.enableSimpleBroker("/sub");
        /*
        클라이언트가 /pub/chat/message 로 전송
        → 서버의 @MessageMapping("/chat/message") 가 받음
         */
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Stomp WebSocket Endpoint 설정
        //ws://localhost:8080/ws-stomp
        //WebSocket 최초 연결 주소를 /ws-stomp 로 설정
        registry.addEndpoint("/ws-stomp")
                //.setAllowedOrigins("*") //CORS 설정 모든 도메인 연결 하용
                .setAllowedOriginPatterns("*")
                .withSockJS(); //SockJS 폴백을 활성화
    }
}
