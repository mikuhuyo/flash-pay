package com.flash.transaction.controller;

import com.alibaba.fastjson.JSONObject;
import com.flash.common.domain.BrowserType;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.AmountUtil;
import com.flash.common.util.EncryptUtil;
import com.flash.common.util.IPUtil;
import com.flash.common.util.StringUtil;
import com.flash.merchant.api.IAppService;
import com.flash.merchant.api.dto.AppDto;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;
import com.flash.transaction.api.ITransactionService;
import com.flash.transaction.api.dto.PayOrderDto;
import com.flash.transaction.api.vo.OrderConfirmVo;
import com.flash.transaction.convert.PayOrderConvert;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Controller
public class PayController {

    @Reference
    private IAppService iAppService;
    @Reference
    private ITransactionService iTransactionService;

    @ApiOperation("支付宝门店下单付款")
    @PostMapping("/createAliPayOrder")
    public void createAliPayOrderForStore(OrderConfirmVo orderConfirmVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtil.isBlank(orderConfirmVO.getAppId())) {
            throw new BusinessException(CommonErrorCode.E_300003);
        }

        orderConfirmVO.setStoreId(orderConfirmVO.getStoreId().replace(",", ""));

        PayOrderDto payOrderDTO = PayOrderConvert.INSTANCE.vo2dto(orderConfirmVO);
        payOrderDTO.setTotalAmount(Integer.valueOf(AmountUtil.changeY2F(orderConfirmVO.getTotalAmount())));
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request));
        // 获取下单应用信息
        AppDto app = iAppService.getAppById(payOrderDTO.getAppId());
        // 设置所属商户
        payOrderDTO.setMerchantId(app.getMerchantId());
        PaymentResponseDTO<Object> payOrderResult = (PaymentResponseDTO<Object>) iTransactionService.submitOrderByAliPay(payOrderDTO);
        String content = String.valueOf(payOrderResult.getContent());

        log.info("支付宝H5支付响应的结果: {}", content);
        response.setContentType("text/html;charset=UTF‐8");
        response.getWriter().write(content);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @GetMapping("/pay-entry/{ticket}")
    public String payEntry(@PathVariable("ticket") String ticket, HttpServletRequest request, ModelMap modelMap) {
        // 获取tick
        String tickString = EncryptUtil.decodeUTF8StringBase64(ticket);

        PayOrderDto payOrderDto = JSONObject.parseObject(tickString, PayOrderDto.class);

        // 获取浏览器类型
        BrowserType browserType = BrowserType.valueOfUserAgent(request.getHeader("user-agent"));

        try {
            if (browserType == BrowserType.ALIPAY) {
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
