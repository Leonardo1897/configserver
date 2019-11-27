package com.tencent.configserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sandylian
 * @date 2019/11/26 20:42
 **/
@Data
@ConfigurationProperties(prefix = "spring.cloud.config.server.native")
public class SearchLocation {
    private List<String> searchLocations;

}
