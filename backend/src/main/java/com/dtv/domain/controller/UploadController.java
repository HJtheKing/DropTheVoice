package com.dtv.domain.controller;

import com.dtv.domain.entity.ProcessedVoice;
import com.dtv.domain.entity.Voice;
import com.dtv.domain.entity.VoiceType;
import com.dtv.domain.repository.VoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dtv.domain.dto.request.VoiceCreateRequestDTO;
import com.dtv.domain.service.MemberService;
import com.dtv.domain.service.RedisService;
import com.dtv.domain.service.SpreadService;
import com.dtv.domain.service.VoiceUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api-upload")
@RequiredArgsConstructor
public class UploadController {

    private final VoiceUploadService voiceUploadService;
    private final MemberService memberService;
    private final VoiceRepository voiceRepository;
    private final RedisService redisService;
    private final SpreadService spreadService;

    Random random = new Random();

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
        voice.setVoiceType(VoiceType.valueOf(voiceType));
        voice.setImageUrl("https://picsum.photos/id/" + random.nextInt(300) + "/200/300");
        voiceRepository.save(voice);

        // voiceType이 poke일 경우 확산 X, Redis에 저장(음성 찾기 기능 위해서)
        if(voiceType.equals("pokemon")){
            redisService.addLocation(RedisService.VOICE_KEY, RedisService.VOICE_TIME_KEY, voice.getVoiceId(), longitude, latitude, 24);
        }
        else {  // virus의 경우 확산 O, 온라인 유저 : WebRTC, 오프라인 유저 : Redis 저장 멤버 중 최근 접속 시간 한 시간 내
            long voiceId = voice.getVoiceId();

            spreadService.spreadLogic(longitude, latitude, voiceId, memberId);
        }
        return new ResponseEntity<>(voice, HttpStatus.CREATED);
    }

    /**
     * Flask에서 처리된 데이터를 수신
     */
    @PostMapping("/receive_processed_audio")
    public String receiveProcessedAudio(@RequestBody ProcessedVoice dto) {
        // 처리된 데이터 출력
        log.info("Where is processed Audio? : {} ", dto.getProcessedPath());
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
