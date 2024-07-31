package com.ssafy.a505.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.a505.domain.dto.request.VoiceCreateRequestDTO;
import com.ssafy.a505.domain.entity.Voice;

public interface UploadService {

    Voice uploadAndSendVoice(VoiceCreateRequestDTO voiceCreateRequestDTO, float pitchShift) throws JsonProcessingException;

    String sendToFlask(Voice voice, float pitchShift) throws JsonProcessingException;

}
