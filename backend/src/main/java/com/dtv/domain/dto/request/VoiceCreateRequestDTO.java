package com.dtv.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class VoiceCreateRequestDTO {
    Long memberId;
    String title;
    MultipartFile audioFile;

}
