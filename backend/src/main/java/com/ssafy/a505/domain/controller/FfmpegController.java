package com.ssafy.a505.domain.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/audio")
public class AudioController {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    /**
     * 파일 업로드 및 처리
     *
     * @param file 업로드 및 처리할 오디오 파일
     * @return 처리된 오디오 파일을 포함하는 ResponseEntity
     * @throws IOException 파일 처리 중 I/O 오류가 발생할 경우
     *
     * 전제조건:
     * - file 파라미터는 null이 아니어야 함
     * - file은 유효한 오디오 파일이어야 함
     *
     * 사후조건:
     * - 업로드된 파일은 "uploaded.mp3"로 저장됨
     * - 처리된 파일을 응답으로 반환
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("파일 업로드 요청 수신");

        // 업로드된 파일 저장
        File mp3File = new File("uploaded.mp3");
        try (FileOutputStream fos = new FileOutputStream(mp3File)) {
            fos.write(file.getBytes());
        }

        // 파일 변조
        File modulatedFile = modulateAudio(mp3File);

        // 변조된 파일을 바이트 배열로 변환
        byte[] audioBytes = IOUtils.toByteArray(new FileInputStream(modulatedFile));

        // 처리된 파일로 HTTP 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "modulated.mp3");

        log.info("파일 업로드 및 처리 완료");

        return new ResponseEntity<>(audioBytes, headers, HttpStatus.OK);
    }

    /**
     * 소음 감소 및 목소리 강조 필터를 적용
     *
     * @param inputFile 처리할 입력 오디오 파일
     * @return 처리된 오디오 파일
     * @throws IOException 처리 중 I/O 오류가 발생할 경우
     *
     * 전제조건:
     * - inputFile 파라미터는 유효한 파일이어야 함
     *
     * 사후조건:
     * - inputFile 처리 후 "modulated.mp3"로 저장됨
     */
    private File modulateAudio(File inputFile) throws IOException {
        log.info("오디오 변조 시작");

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffmpegPath);

        File outputFile = new File("modulated.mp3");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(outputFile.getAbsolutePath())
                .setAudioFilter("afftdn=nf=-25,equalizer=f=1000:width_type=o:width=2:g=5") // 소음 감소 및 목소리 강조 필터 체인
                .setFormat("mp3")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        log.info("오디오 변조 완료");

        return outputFile;
    }
}
