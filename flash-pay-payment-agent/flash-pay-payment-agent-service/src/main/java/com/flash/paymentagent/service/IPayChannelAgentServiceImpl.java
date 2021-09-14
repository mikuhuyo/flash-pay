package com.flash.paymentagent.service;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.paymentagent.api.IPayChannelAgentService;
import com.flash.paymentagent.api.conf.AliConfigParam;
import com.flash.paymentagent.api.dto.AlipayBean;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service(interfaceClass = IPayChannelAgentService.class)
public class IPayChannelAgentServiceImpl implements IPayChannelAgentService {
    @Override
    public PaymentResponseDTO<Object> createPayOrderByAliWap(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException {
        System.out.println(aliConfigParam);
        System.out.println(alipayBean);

        System.out.println(aliConfigParam.rsaPrivateKey.length());

        log.info("支付宝配置参数: {}", aliConfigParam.toString());
        log.info("支付宝请求参数: {}", alipayBean.toString());

        // 获取支付宝支付渠道参数
        // 支付宝下单接口地址
        String gateway = aliConfigParam.getUrl();
        // app id
        String appId = aliConfigParam.getAppId();
        // 私钥
        String rsaPrivateKey = aliConfigParam.getRsaPrivateKey();
        // 数据格式json
        String format = aliConfigParam.getFormat();
        // 字符编码
        String charest = aliConfigParam.getCharest();
        // 公钥
        String alipayPublicKey = aliConfigParam.getAlipayPublicKey();
        // 签名算法类型
        String signtype = aliConfigParam.getSigntype();
        // 支付结果通知地址
        String notifyUrl = aliConfigParam.getNotifyUrl();
        // 支付完成返回商户地址
        String returnUrl = aliConfigParam.getReturnUrl();

        // 支付宝sdk客户端
        // DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest, alipayPublicKey, signtype);
        DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest, alipayPublicKey, "RSA");

        // 封装请求支付信息
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // JSONObject json = new JSONObject();
        // json.put("out_trade_no", alipayBean.getOutTradeNo());
        // json.put("subject", alipayBean.getSubject());
        // json.put("total_amount", alipayBean.getTotalAmount());
        // json.put("time_expire", alipayBean.getExpireTime());
        // json.put("product_code", alipayBean.getProductCode());
        // alipayRequest.setBizContent(json.toJSONString());

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        // 闪聚平台订单
        model.setOutTradeNo(alipayBean.getOutTradeNo());
        // 订单标题
        model.setSubject(alipayBean.getSubject());
        // 订单金额
        model.setTotalAmount(alipayBean.getTotalAmount());
        // 订单过期时间
        model.setTimeoutExpress(alipayBean.getExpireTime());
        // 商户与支付宝签定的产品码, 固定为QUICK_WAP_WAY
        model.setProductCode(alipayBean.getProductCode());
        // 请求参数集合
        alipayRequest.setBizModel(model);

        // 设置同步地址
        alipayRequest.setReturnUrl(returnUrl);
        // 设置异步地址
        alipayRequest.setNotifyUrl(notifyUrl);

        try {
            // 调用SDK提交表单
            AlipayTradeWapPayResponse alipayTradeWapPayResponse = defaultAlipayClient.pageExecute(alipayRequest);

            log.info("支付宝手机网站支付预支付订单信息: {}", alipayTradeWapPayResponse);

            PaymentResponseDTO<Object> res = new PaymentResponseDTO<Object>();
            res.setContent(alipayTradeWapPayResponse.getBody());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            // 支付宝确认支付失败
            throw new BusinessException(CommonErrorCode.E_400002);
        }
    }
}
