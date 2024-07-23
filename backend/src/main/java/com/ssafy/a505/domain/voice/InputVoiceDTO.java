package com.ssafy.a505.domain.voice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InputVoiceDTO {
    String title;
    MultipartFile audioFile;
}
