package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Pick;
import com.ssafy.a505.domain.repository.PickRepository;
import com.ssafy.a505.global.sse.NotificationController;
import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Heart;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.HeartRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import com.ssafy.a505.global.sse.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceServiceImpl implements VoiceService{
    private final HeartRepository heartRepository;
    private final VoiceRepository voiceRepository;
    private final NotificationService notificationService;
    private final PickRepository pickRepository;

    @Override
    public List<Voice> getVoiceOrderByHeartCountDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime ago = LocalDateTime.now().minusDays(2);
        return voiceRepository.findAllByOrderByHeartCountDesc(ago ,pageable);
    }

    @Override
    public List<Voice> getAllVoice() {
        return voiceRepository.findAll();
    }

    @Override
    public List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable) {
        return voiceRepository.findByMemberWithHeart(memberId, pageable);
    }

    @Override
    public List<Voice> findByMemberWithPick(Long memberId, Pageable pageable) {
        return voiceRepository.findByMemberWithPick(memberId, pageable);
    }

    @Override
    public List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable) {
        return voiceRepository.findByMemberWithSpread(memberId, pageable);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long voiceId, Member member) {
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));

        Optional<Heart> existingHeart = heartRepository.findByVoiceAndMember(voice, member);
        if (existingHeart.isPresent()) {
            // 좋아요가 이미 있는 경우: 좋아요 취소
            heartRepository.delete(existingHeart.get());
            voice.setHeartCount(voice.getHeartCount() - 1);
            return false;
        } else {
            // 좋아요가 없는 경우: 좋아요 추가
            Heart heart = new Heart(voice, member);
            heartRepository.save(heart);
            voice.setHeartCount(voice.getHeartCount() + 1);
            // SSE 알림 전송
            notificationService.sendNotification(member.getMemberId(), "Liked");
            return true;
        }
    }

    @Override
    @Transactional
    public boolean togglePick(Long voiceId, Member member) {
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));
        Optional<Pick> existingPick = pickRepository.findByVoiceAndMember(voice, member);
        if (existingPick.isPresent()) {
            pickRepository.delete(existingPick.get());
            return false;
        } else {
            Pick pick = new Pick(voice, member);
            pickRepository.save(pick);
            notificationService.sendNotification(member.getMemberId(), "Picked");
            return true;
        }
    }

    @Override
    public Voice getVoiceById(Long voiceId) {
        Voice result = voiceRepository.findById(voiceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));
        result.setListenCount(result.getListenCount() + 1);
        return voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));
    }

    @Override
    public List<VoiceResponseDTO> getNearbyVoices(double latitude, double longitude, double radius, Member member) {
        List<Voice> voices = voiceRepository.findNearbyVoices(latitude, longitude, radius);
        return voices.stream()
                .map(voice -> VoiceResponseDTO.fromEntity(voice, member)).collect(Collectors.toList());
    }

    public Page<VoiceResponseDTO> searchVoices(String keyword, int page, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, getSort(sort));
        return voiceRepository.findVoicesWithKeyword(keyword, pageRequest).map(VoiceResponseDTO::fromEntity);
    }


    private Sort getSort(String sort) {
        switch (sort) {
            case "listenCount":
                return Sort.by(Sort.Direction.DESC, "listenCount");
            case "heartCount":
                return Sort.by(Sort.Direction.DESC, "heartCount");
            case "latest":
            default:
                return Sort.by(Sort.Direction.DESC, "dateTime");
        }
    }
}
