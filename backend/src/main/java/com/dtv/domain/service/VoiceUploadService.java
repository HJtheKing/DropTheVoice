package com.dtv.domain.service;

import com.dtv.domain.entity.Voice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dtv.domain.dto.request.VoiceCreateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface VoiceUploadService {

    Voice uploadAndSendVoice(VoiceCreateRequestDTO voiceCreateRequestDTO, float pitchShift) throws JsonProcessingException;

    String uploadAndSendImg(MultipartFile imgFile) throws JsonProcessingException;

    String sendToFlask(Voice voice, float pitchShift) throws JsonProcessingException;

}
