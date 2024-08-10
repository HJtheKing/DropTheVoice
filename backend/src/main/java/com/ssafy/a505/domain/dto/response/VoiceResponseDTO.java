package com.ssafy.a505.domain.dto.response;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VoiceResponseDTO {

    private long voiceId;
    private Long memberId;
    private Long heartCount;
    private Long listenCount;
    private String title;
    private String imageUrl;
    private String originalName;
    private String savePath;
    private String saveFolder;
    private double latitude;
    private double longitude;
    private LocalDateTime dateTime;
    private VoiceType voiceType;
    private boolean isProcessed;
    private String processedPath;

    // 다른 필요한 필드들도 포함 가능

    public static VoiceResponseDTO fromEntity(Voice voice) {
        return VoiceResponseDTO.builder()
                .voiceId(voice.getVoiceId())
                .memberId(voice.getMember().getMemberId())
                .heartCount(voice.getHeartCount())
                .listenCount(voice.getListenCount())
                .title(voice.getTitle())
                .imageUrl(voice.getImageUrl())
                .originalName(voice.getOriginalName())
                .savePath(voice.getSavePath())
                .saveFolder(voice.getSaveFolder())
                .latitude(voice.getLatitude())
                .longitude(voice.getLongitude())
                .dateTime(voice.getDateTime())
                .voiceType(voice.getVoiceType())
                .isProcessed(voice.isProcessed())
                .processedPath(voice.isProcessed() && !voice.getProcessedVoicePath().isEmpty() ? voice.getProcessedVoices().get(0).getProcessedPath() : null)
                .build();
    }


}
