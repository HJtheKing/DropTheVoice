package com.ssafy.a505.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.a505.domain.dto.request.VoiceCreateRequestDTO;
import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.ProcessedVoice;
import com.ssafy.a505.domain.entity.Spread;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.SpreadRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.domain.service.MemberService;
import com.ssafy.a505.domain.service.RedisService;
import com.ssafy.a505.domain.service.VoiceUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api-upload")
@RequiredArgsConstructor
public class UploadController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VoiceUploadService voiceUploadService;
    private final MemberService memberService;
    private final VoiceRepository voiceRepository;
    private final RedisService redisService;
    private final SpreadRepository spreadRepository;
    private final MemberRepository memberRepository;

    /**
     * Flask로 데이터 전송
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadVoice(@RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                         @RequestParam(value = "memberId") Long memberId,
                                         @RequestPart(value = "title") String title, @RequestParam(value = "latitude", required = false) Double latitude, @RequestParam(value = "longitude", required = false) Double longitude, @RequestParam(value = "voiceType") String voiceType,
                                         @RequestParam("pitchShift") float pitchShift) throws JsonProcessingException {
        VoiceCreateRequestDTO voiceCreateRequestDTO = new VoiceCreateRequestDTO(memberId, title, audioFile);
        Voice voice = voiceUploadService.uploadAndSendVoice(voiceCreateRequestDTO, pitchShift);
        log.info("latitude: {}, longitude: {}, voiceType: {}, memberId: {}", latitude, longitude, voiceType, memberId);
        voice.setLatitude(latitude);
        voice.setLongitude(longitude);
        voiceRepository.save(voice);
        // voiceType이 poke일 경우 확산 X, Redis에 저장(음성 찾기 기능 위해서)
        if(voiceType.equals("pokemon")){
            redisService.addLocation(RedisService.VOICE_KEY, RedisService.VOICE_TIME_KEY, voice.getVoiceId(), longitude, latitude, 24);
        }
        else {  // virus의 경우 확산 O, 온라인 유저 : WebRTC, 오프라인 유저 : Redis 저장 멤버 중 최근 접속 시간 한 시간 내
            // 현재 접속 중 유저 반환
            Set<String> wsMemberIds = redisService.getWsMemberIds();
            for (String wsMemberId : wsMemberIds) {
                log.info("wsMemberId: {}", wsMemberId);
            }
            // 접속 중인 유저 중 반경 내 있는 유저 반환
            Set<RedisResponseDTO> findMembers = redisService.getMembersByRadiusV2(longitude, latitude, 1d, voice.getVoiceId(), 5, wsMemberIds);
            Set<String> wsMembersInRadius = findMembers.stream()
                    .map(dto -> dto.getId().toString())
                    .collect(Collectors.toSet());
            /**
             * markReceived 로직 추가
             */
            for (String membersInRadius : wsMembersInRadius) {
                log.info("membersInRadius: {}", membersInRadius);
                redisService.markReceived(voice.getVoiceId(), Long.valueOf(membersInRadius));
            }

            // 접속 중인 반경 내 유저 memberId 발행
            messagingTemplate.convertAndSend("/topic/others/"+memberId,wsMembersInRadius);

            /**
             * 접속 한 시간 이내의 오프라인 유저에게 전송 로직 추가
             */
            Set<RedisResponseDTO> membersByRadius = redisService.getMembersByRadius(longitude, latitude, 1d, voice.getVoiceId(), 5);
            for (RedisResponseDTO byRadius : membersByRadius) {
                Spread spread = new Spread();
                Member findMember = memberRepository.findByMemberId(byRadius.getId()).get();
                Voice findVoice = voiceRepository.findById(voice.getVoiceId()).get();
                spread.setMember(findMember);
                spread.setVoice(findVoice);
                spreadRepository.save(spread);
            }
        }
        return new ResponseEntity<>(voice, HttpStatus.CREATED);
    }

    /**
     * Flask에서 처리된 데이터를 수신
     */
    @PostMapping("/receive_processed_audio")
    public String receiveProcessedAudio(@RequestBody ProcessedVoice dto) {
        // 처리된 데이터 출력
        System.out.println("Where is processed Audio? : " + dto.getProcessedPath());

        return "";
    }

    @PostMapping("/change")
    public String changeAudioFileAndSpread(
            @ModelAttribute VoiceCreateRequestDTO audioInput) {
        VoiceCreateRequestDTO newVoiceInput = changeVoice(audioInput);
        return "" + newVoiceInput;
    }

    private VoiceCreateRequestDTO changeVoice(VoiceCreateRequestDTO audioInput){
        return audioInput;
    }

    @GetMapping("/remain-voice-change/{member_id}")
    public ResponseEntity<?> getRemainVoiceChangeOpportunity(@PathVariable("member_id") Long memberId) {
        int remainOpportunity = memberService.findRemainChangeCount(memberId);
        return ResponseEntity.ok(remainOpportunity);
    }
}
