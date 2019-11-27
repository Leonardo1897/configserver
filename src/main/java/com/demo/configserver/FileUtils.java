package com.demo.configserver;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author sandylian
 * @date 2019/11/26 22:18
 **/
@Slf4j
public class FileUtils {

    public static String filterDir(String dir){
        //dir = dir.substring(dir.indexOf("/"));
        int length = dir.length();
        boolean flag = false;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = 0; i < length; i++){
            char c = dir.charAt(i);
            if(c == '/'){
                if(flag){
                    continue;
                }else {
                    stringBuilder.append(c);
                    flag = true;
                }
            }else {
                stringBuilder.append(c);
                flag = false;
            }
        }

        return stringBuilder.toString();
    }

    public static boolean createDir(String path){
        File file =  new File(path);
        if(file.exists()){
            return true;
        }
        log.info("path: {}",path);
        String fatherPath = path.substring(0,path.lastIndexOf("/"));
        file = new File(fatherPath);
        if(file.exists()){
            file = new File(path);
            file.mkdir();
            return true;
        }else {
            return createDir(fatherPath) && createDir(path);
        }
    }
}
