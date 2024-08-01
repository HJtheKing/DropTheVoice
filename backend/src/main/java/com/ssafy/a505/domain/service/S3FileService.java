package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.VoiceType;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {

    String getFileUrl(String fileName);

    String uploadFile(MultipartFile file, VoiceType category);

    void deleteFile(String fileAddress);

    byte[] downloadFile(String fileAddress);

    String getDownloadFileName(String fileName);

    String getFileFolder(VoiceType category);

}
