package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Spread;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.SpreadRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.global.sse.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpreadService {
    private final SpreadRepository spreadRepository;
    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;
    private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public void spreadLogic(Double longitude, Double latitude, Long voiceId, Long memberId){
        // 현재 접속 중 유저 반환
        Set<String> wsMemberIds = redisService.getWsMemberIds();
        // 접속 중인 유저 중 반경 내 있는 유저 반환
        Set<RedisResponseDTO> findMembers = redisService.getMembersByRadiusV2(longitude, latitude, 10d, voiceId, 5, wsMemberIds);

        for (RedisResponseDTO findMember : findMembers) {
            messagingTemplate.convertAndSend("/topic/voiceId/" + findMember.getId(), voiceId);
        }
        Set<String> set = findMembers.stream()
                .map(dto -> dto.getId().toString())
                .collect(Collectors.toSet());

        for (String m : set) {
            redisService.markReceived(voiceId, Long.valueOf(m));
            if(Long.parseLong(m) == memberId) continue;
            Spread spread = new Spread();
            Member findMember = memberRepository.findByMemberId(Long.parseLong(m)).get();
            Voice findVoice = voiceRepository.findById(voiceId).get();
            spread.setMember(findMember);
            spread.setVoice(findVoice);
            spreadRepository.save(spread);
            notificationService.sendNotification(Long.parseLong(m), "Spread");
            log.info("sendNotification in RTC");
        }

        // 접속 중인 반경 내 유저 memberId 발행
        messagingTemplate.convertAndSend("/topic/others/"+memberId,set);

        /**
         * 접속 한 시간 이내의 오프라인 유저에게 전송 로직 추가
         */
        Set<RedisResponseDTO> membersByRadius = redisService.getMembersByRadius(longitude, latitude, 10d, voiceId, 5);
        for (RedisResponseDTO byRadius : membersByRadius) {
            if(byRadius.getId().equals(memberId)) continue;
            log.info("byRadius.getId = {}, memberId = {}", byRadius.getId(), memberId);
            Spread spread = new Spread();
            Member findMember = memberRepository.findByMemberId(byRadius.getId()).get();
            Voice findVoice = voiceRepository.findById(voiceId).get();
            spread.setMember(findMember);
            spread.setVoice(findVoice);
            spreadRepository.save(spread);
            notificationService.sendNotification(byRadius.getId(), "Spread");
            log.info("sendNotification in OffLine");
            redisService.markReceived(voiceId, byRadius.getId());
            findMember.setHasNew(true);
        }
    }
}
