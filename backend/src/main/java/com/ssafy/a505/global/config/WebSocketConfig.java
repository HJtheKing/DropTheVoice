package com.ssafy.a505.global.config;

import com.ssafy.a505.global.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RedisUtils redisUtils;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //클라이언트로 메세지를 응답 해 줄 때 prefix 정의 - 클라이언트가 메세지를 받을 때
        registry.enableSimpleBroker("/topic"); //ex) stomp.subscribe("/sub/chat/room/",function(){})
        //클라이언트에서 메세지 송신 시 붙일 prefix 정의 - 클라이언트가 메세지를 보낼때
        registry.setApplicationDestinationPrefixes("/ws"); //ex) stomp.send("/sub/chat/room/",function(){})
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //the url is for Websocket handshake
        registry.addEndpoint("/stomp/handshake") //handshake가 될 endpoint지정
                .setAllowedOriginPatterns("*") // 도메인 패턴 사용
                .withSockJS(); //SockJS사용
        //.setAllowedOriginPatterns("http://localhost:3000") // 도메인 패턴 사용
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MyChannelInterceptor());
    }

    public class MyChannelInterceptor implements ChannelInterceptor {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            // STOMP 연결 시에 전처리 작업 수행
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
            if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {

                //"연결 성공 전처리해보자"
                String sessionId = headerAccessor.getSessionId();
                log.info("연결 수립 세션 ID : "+sessionId);
            }
            return ChannelInterceptor.super.preSend(message, channel);
        }

        //메시지 전송후 인터셉터
        @Override
        public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
            StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
            if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
                //{simpMessageType=DISCONNECT, stompCommand=DISCONNECT, simpSessionAttributes={},
                // simpSessionId=pzvyh0aw}

                String sessionId = headerAccessor.getSessionId();
                log.info("연결 종료 세션 ID : "+sessionId);
            }
        }
    }
}
