package com.ssafy.a505.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class VoiceCreateRequestDTO {
    String title;
    MultipartFile audioFile;
}
