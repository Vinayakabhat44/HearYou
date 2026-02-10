package com.antigravity.media.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, String folder);

    void deleteFile(String fileUrl);
}
