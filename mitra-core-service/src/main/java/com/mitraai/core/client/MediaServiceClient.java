package com.mitraai.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "mitra-core-service", contextId = "mediaServiceClient")
public interface MediaServiceClient {

    @PostMapping(value = "/api/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("folder") String folder);
}
