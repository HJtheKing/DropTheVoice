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
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/audio")
public class FfmpegController {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일 저장
        File mp3File = new File("uploaded.mp3");
        try (FileOutputStream fos = new FileOutputStream(mp3File)) {
            fos.write(file.getBytes());
        }

        // 파일 변조
        File modulatedFile = modulateAudio(mp3File);

        // 변조된 파일을 바이트 배열로 변환
        byte[] audioBytes = IOUtils.toByteArray(new FileInputStream(modulatedFile));

        // HTTP 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "modulated.mp3");
        return new ResponseEntity<>(audioBytes, headers, HttpStatus.OK);
    }

    private File modulateAudio(File inputFile) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffmpegPath);

        File outputFile = new File("modulated.mp3");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(outputFile.getAbsolutePath())
                .setAudioFilter("afftdn=nf=-25 equalizer=f=1000:width_type=o:width=2:g=5") // 소음 감소 및 목소리 강조 필터
                .setFormat("mp3")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        return outputFile;
    }
}
