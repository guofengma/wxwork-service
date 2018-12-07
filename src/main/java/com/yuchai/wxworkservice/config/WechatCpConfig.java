package com.yuchai.wxworkservice.config;

import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.WxCpInMemoryConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信服务类
 */
@Component
public class WechatCpConfig {
    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean
    public WxCpService wxCpService() {
        WxCpService wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage());
        return wxCpService;
    }

    @Bean
    public WxCpConfigStorage wxCpConfigStorage() {
        WxCpConfigStorage wxCpConfigStorage = new WxCpInMemoryConfigStorage();
        ((WxCpInMemoryConfigStorage) wxCpConfigStorage).setCorpId(accountConfig.getCorpId());
        ((WxCpInMemoryConfigStorage) wxCpConfigStorage).setAgentId(accountConfig.getAgentid());
        ((WxCpInMemoryConfigStorage) wxCpConfigStorage).setCorpSecret(accountConfig.getSecret());
        return wxCpConfigStorage;
    }
}
