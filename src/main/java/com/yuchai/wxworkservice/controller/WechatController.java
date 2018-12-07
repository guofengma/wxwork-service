package com.yuchai.wxworkservice.controller;


import com.yuchai.wxworkservice.config.WechatAccountConfig;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpMessageSendResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 *
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/wechat")
public class WechatController {
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private WxCpConfigStorage wxCpConfigStorage;

    @Autowired
    private WechatAccountConfig accountConfig;

    /**
     * 微信网页授权auth获取用户openId
     *
     * @param redirectUrl 认证通过后携带用户openId的网页URL
     * @param extendParam 可以携带一个备用参数
     * @return 跳转到输入参数的URL并在URL后面携带用户openId
     */
    @GetMapping("/authorize")
    public void authorize(@RequestParam String redirectUrl,
                          @RequestParam String extendParam,
                          HttpServletResponse response) throws IOException {
        String result = wxCpService.getOauth2Service().buildAuthorizationUrl(accountConfig.getAuthServiceUrl(), URLEncoder.encode(redirectUrl + "?extendParam=" + extendParam));
//        return "redirect:" + result;
        response.sendRedirect(result);
    }

    /**
     * 微信网页授权auth获取用户openId时微信后台携带code跳转的方法
     *
     * @param code        企业微信返回的code
     * @param redirectUrl 企业微信返回的state
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/getUserInfo")
    public ModelAndView getUserInfo(@RequestParam String code,
                                    @RequestParam("state") String redirectUrl) throws WxErrorException, UnsupportedEncodingException {
        String[] res = wxCpService.getOauth2Service().getUserInfo(code);
        String userId = res[0];
        WxCpUser user = wxCpService.getUserService().getById(userId);
        return new ModelAndView(new RedirectView(redirectUrl + "&openid=" + userId
                + "&name=" + URLEncoder.encode(user.getName(), "utf-8")
                + "&avatar=" + user.getAvatar()));
    }


    /**
     * JS-SDK获取签名信息接口
     *
     * @param url 注册签名信息URL
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/getJsapiSignature")
    public WxJsapiSignature getJsapiSignature(@RequestParam String url) throws WxErrorException {
        return wxCpService.createJsapiSignature(url);
    }


    /**
     * 发送卡片信息
     *
     * @param toUser      用户列表 "UserID1|UserID2|UserID3"
     * @param title       卡片主题
     * @param description 卡片内容
     * @param url         跳转URL
     * @return 结果
     * @throws WxErrorException
     */
    @PostMapping("/sendTextCardMsg")
    public WxCpMessageSendResult sendTextCardMsg(@RequestParam String toUser,
                                                 @RequestParam String title,
                                                 @RequestParam String description,
                                                 @RequestParam String url) throws WxErrorException {

        WxCpMessage wxCpMessage = WxCpMessage.TEXTCARD()
                .toUser(toUser)
                .title(title)
                .description(description)
                .url(url)
                .build();
        return wxCpService.messageSend(wxCpMessage);
    }

}
