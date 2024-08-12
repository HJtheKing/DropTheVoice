package com.ssafy.a505.global.service;

import com.ssafy.a505.domain.entity.VoiceType;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {

    String getFileUrl(String fileName);

    String uploadFile(MultipartFile file, boolean isProcessed);

    void deleteFile(String fileAddress);

    byte[] downloadFile(String fileAddress);

    String getDownloadFileName(String fileName);

    String getFileFolder(boolean isProcessed);

}
