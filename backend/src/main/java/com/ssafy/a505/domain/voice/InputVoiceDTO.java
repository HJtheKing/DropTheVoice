package com.ssafy.a505.domain.Voice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InputVoiceDTO {
    String title;
    MultipartFile audioFile;
}
