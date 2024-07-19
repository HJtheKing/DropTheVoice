package com.ssafy.a505.Config.WebSocket;

import com.ssafy.a505.Domain.Member;
import com.ssafy.a505.Domain.Voice.Voice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/")
@CrossOrigin(origins = "*", allowCredentials = "true")
@Slf4j
public class StompController {
    private final SimpMessagingTemplate messagingTemplate;

    public Set<String> sessionIDs;

    public StompController(SimpMessagingTemplate simpMessagingTemplate){
        sessionIDs = new HashSet<>();
        this.messagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping(value = "/position")
    public void message(Member member, Message<Member> message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();

        log.info(sessionId+"is sessionId");
        log.info("Session Logged In Member Info: "+member.toString());

        //레디스에 위경도 좌표와 세션ID를 포함해서 저장하자.
        //messagingTemplate.convertAndSend("/topic/messages/" + sessionId, message);
    }

    //camKey 를 받기위해 신호를 보내는 webSocket
    @MessageMapping("/spread/{latitude}/{longitude}")
    public void spread(@Payload String sessionId,@DestinationVariable(value = "latitude") double latitude, @DestinationVariable(value = "longitude") double longitude) {
        log.info("[Key] : {}  [lat,lng] : {} : {}", sessionId,latitude,longitude);
        //음원 발송 위경도 기준으로
        //상대 세션ID 탐색
        sessionIDs.add(sessionId);
        messagingTemplate.convertAndSend("/topic/others/"+sessionId,sessionIDs);
        for(String session: sessionIDs){
            System.out.println("in map "+session);
        }
        
        //상대 세션ID 리스트 (3개이상) 반환
    }

    @MessageMapping("/peer/offer/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/offer/{otherSessionId}")
    public String PeerHandleOffer(@Payload String offer, @DestinationVariable(value = "mySessionId") String mySessionId,
                                  @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[OFFER] {} : {}", mySessionId+" : "+otherSessionId, offer);

        return offer;
    }

    @MessageMapping("/peer/answer/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/answer/{otherSessionId}")
    public String PeerHandleAnswer(@Payload String answer, @DestinationVariable(value = "mySessionId") String mySessionId,
                                   @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[ANSWER] {} : {}", mySessionId+" : "+otherSessionId, answer);

        return answer;
    }

    //iceCandidate 정보를 주고 받기 위한 webSocket
    //camKey : 각 요청하는 캠의 key , roomId : 룸 아이디
    @MessageMapping("/peer/iceCandidate/{mySessionId}/{otherSessionId}")
    @SendTo("/topic/peer/iceCandidate/{otherSessionId}")
    public String PeerHandleIceCandidate(@Payload String candidate, @DestinationVariable(value = "mySessionId") String mySessionId,
                                         @DestinationVariable(value = "otherSessionId") String otherSessionId) {
        log.info("[ICECANDIDATE] {} : {}", mySessionId+" : "+otherSessionId, candidate);

        return candidate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        sessionIDs.remove(sessionId);

        System.out.println("Disconnected: " + sessionId);
        System.out.println(sessionIDs.toString());
    }
}
