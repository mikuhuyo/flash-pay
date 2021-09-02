package com.flash.transaction.controller;

import com.alibaba.fastjson.JSONObject;
import com.flash.common.domain.BrowserType;
import com.flash.common.util.EncryptUtil;
import com.flash.transaction.api.dto.PayOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Controller
public class PayController {
    @RequestMapping("/pay-entry/{ticket}")
    public String payEntry(@PathVariable("ticket") String ticket, HttpServletRequest request, ModelMap modelMap) {
        // 获取tick
        String tickString = EncryptUtil.decodeUTF8StringBase64(ticket);

        PayOrderDto payOrderDto = JSONObject.parseObject(tickString, PayOrderDto.class);

        // 获取浏览器类型
        BrowserType browserType = BrowserType.valueOfUserAgent(request.getHeader("user-agent"));

        try {
            if (browserType == BrowserType.ALIPAY) {
                // {"appId":"ce163cbb8af44cef9396e151ecc6f3c1",
                // "body":"向菜狗子研究院付款",
                // "channel":"shanju_c2b",
                // "merchantId":1431583860521963522,
                // "storeId":1431583860576489473,"
                // subject":"菜狗子研究院商品"}

                // String s = ParseURLPairUtil.parseURLPair(payOrderDto);
                // return "pay?" + s;

                modelMap.addAttribute("appId", payOrderDto.getAppId());
                modelMap.addAttribute("channel", payOrderDto.getChannel());
                modelMap.addAttribute("subject", payOrderDto.getSubject());
                modelMap.addAttribute("body", payOrderDto.getBody());
                modelMap.addAttribute("merchantId", payOrderDto.getMerchantId());
                modelMap.addAttribute("storeId", payOrderDto.getStoreId());

                return "pay";
            } else {
                log.info("你在逗我, 我只做支付宝的, 微信是不可能微信的.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage(), e);
        }

        return "pay_error";
    }
}
