package com.yuchai.wxworkservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件映射类
 * 获取微信配置文件信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    private String corpId;
    private Integer agentid;
    private String secret;
    private String authServiceUrl;
}
