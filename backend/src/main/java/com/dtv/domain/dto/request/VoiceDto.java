package com.dtv.domain.dto.request;

import com.dtv.domain.entity.VoiceType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VoiceDto {
    private String voiceUrl;
    private double latitude;
    private double longitude;
    private VoiceType voiceType;
    MultipartFile voiceFile;

    public VoiceDto(){}
}
