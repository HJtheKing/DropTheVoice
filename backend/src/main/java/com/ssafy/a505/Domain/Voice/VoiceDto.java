package com.ssafy.a505.Domain.Voice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VoiceDto {
//    long id; 세션 id 값으로 넣을 예정
    private String voiceUrl;
    private double latitude;
    private double longitude;
    private VoiceType voiceType;
    MultipartFile voiceFile;

    public VoiceDto(){}
}
