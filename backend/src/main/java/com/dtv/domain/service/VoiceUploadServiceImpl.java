package com.dtv.domain.service;

import com.dtv.domain.repository.MemberRepository;
import com.dtv.domain.repository.VoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dtv.domain.dto.request.VoiceCreateRequestDTO;
import com.dtv.domain.dto.response.ProcessedVoiceResponseDTO;
import com.dtv.domain.entity.Member;
import com.dtv.domain.entity.ProcessedVoice;
import com.dtv.domain.entity.Voice;
import com.dtv.global.execption.CustomException;
import com.dtv.global.execption.ErrorCode;
import com.dtv.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceUploadServiceImpl implements VoiceUploadService {

    private final S3FileService s3FileService;
    private final VoiceRepository voiceRepository;
    private final MemberRepository memberRepository;

    //데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;

    // 플라스크 url 맞춰야함
    @Value("${spring.flask.url}")
    private String flaskUrl;

    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${cloud.aws.s3.bucketName}")
    private String awsBucketName;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    // 목소리 S3 저장
    // 목소리 S3 저장 및 Flask 전송
    public Voice uploadAndSendVoice(VoiceCreateRequestDTO voiceCreateRequestDTO, float pitchShift) throws JsonProcessingException {
        Voice voice = convertToNewEntity(voiceCreateRequestDTO);
        if (voiceCreateRequestDTO.getAudioFile() != null && !voiceCreateRequestDTO.getAudioFile().isEmpty()) {
            voice = uploadAudioFileToS3(voice, voiceCreateRequestDTO.getAudioFile());
        }

        Member member = memberRepository.findById(voiceCreateRequestDTO.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));

        voice.setMember(member);

        voiceRepository.save(voice);

        if (pitchShift == 0) {
            return voice;
        }
        String processedPath = sendToFlask(voice, pitchShift);

        ProcessedVoice processedVoice = new ProcessedVoice();
        processedVoice.setVoice(voice);
        processedVoice.setProcessedPath(processedPath);
        member.setRemainChangeCount(member.getRemainChangeCount() - 1);
        voice.addProcessedVoice(processedVoice);

        voiceRepository.save(voice); // 변경된 Voice 저장
        member.setTotalUploadCount(member.getTotalUploadCount() + 1);
        return voice;
    }

    public String uploadAndSendImg(MultipartFile imgFile){
        String imgUrl = "";
        if(imgFile != null){
            imgUrl = s3FileService.uploadFile(imgFile, 4);
        }
        else log.info("file is null");

        return imgUrl;
    }


    @Transactional
    public String sendToFlask(Voice voice, float pitchShift) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더를 JSON으로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("voice", voice);
        payload.put("aws_access_key", awsAccessKey);
        payload.put("aws_secret_key", awsSecretKey);
        payload.put("aws_bucket_name", awsBucketName);
        payload.put("aws_region", awsRegion);
        payload.put("pitchShift", pitchShift);

        String param = objectMapper.writeValueAsString(payload);

        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        // Flask 서버로 데이터를 전송하고 받은 응답 값 처리
        String response = restTemplate.postForObject(flaskUrl, entity, String.class);

        // 응답으로부터 변조된 파일 경로 추출
        ProcessedVoiceResponseDTO processedVoiceResponseDTO = objectMapper.readValue(response, ProcessedVoiceResponseDTO.class);
        return processedVoiceResponseDTO.getProcessedPath();
    }

    private Voice uploadAudioFileToS3(Voice voice, MultipartFile multipartFile) {
        int dataType;
        if(voice.isProcessed()) dataType = 2;
        else dataType = 1;
//        String saveFile = s3FileService.uploadFile(multipartFile, dataType);
        voice.setSaveFolder(s3FileService.getFileFolder(dataType));
//        voice.setSavePath(saveFile);
        voice.setSavePath("saveFile");
        voice.setOriginalName(multipartFile.getOriginalFilename());
        return voice;
    }

    private Voice convertToNewEntity(VoiceCreateRequestDTO voiceCreateRequestDTO) {
        return Voice.builder()
                .title(voiceCreateRequestDTO.getTitle())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
