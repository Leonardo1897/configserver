package com.tencent.configserver.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sandylian
 * @date 2019/11/26 22:11
 **/
public interface FileStorageService {
    public String storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);
}
