package com.tencent.configserver.service;

import com.tencent.configserver.SearchLocation;
import com.tencent.configserver.exception.FileStorageException;
import com.tencent.configserver.exception.MyFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.locks.ReentrantLock;

import static com.tencent.configserver.FileUtils.filterDir;

/**
 * @author sandylian
 * @date 2019/11/26 22:13
 **/
@Slf4j
@Service("fileSystemStorage")
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private SearchLocation searchLocation;

    private String storageBaseDir = null;

    private Path fileStorageLocation;

    private ReentrantLock reentrantLock = new ReentrantLock();

    public void init() {
        reentrantLock.lock();
        try{
            String patten = "\\{[a-zA-Z]*\\}";
            storageBaseDir = filterDir(searchLocation.getSearchLocations().get(0).replaceAll(patten,"")).substring(5);
            //=========================debug================================
            //storageBaseDir = "C:/Users/sandylian/Desktop/";
            fileStorageLocation = Paths.get(storageBaseDir).normalize();
            log.info("storageBaseDir:{} Final dst: {}",storageBaseDir,fileStorageLocation);
        }catch (Exception e){
            log.error("EXCEPTION: {}",e);
        }finally {
            reentrantLock.unlock();
        }
    }

/*    @Autowired
    public FileStorageServiceImp(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }*/


    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        if(storageBaseDir == null){
            init();
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            if(storageBaseDir == null){
                init();
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
