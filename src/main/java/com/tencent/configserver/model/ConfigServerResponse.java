package com.tencent.configserver.model;

import lombok.Data;

/**
 * @author sandylian
 * @date 2019/11/25 11:12
 **/
@Data
public class ConfigServerResponse {
    private Integer code;
    private String msg;
    private Object data;
}
