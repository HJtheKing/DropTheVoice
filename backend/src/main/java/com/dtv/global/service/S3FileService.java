package com.dtv.global.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {

    String getFileUrl(String fileName);

    String uploadFile(MultipartFile file, int dataType);

    void deleteFile(String fileAddress);

    byte[] downloadFile(String fileAddress);

    String getDownloadFileName(String fileName);

    String getFileFolder(int dataType);

}
