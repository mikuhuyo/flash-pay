package com.flash.paymentagent.message;

import com.alibaba.fastjson.JSON;
import com.flash.paymentagent.api.IPayChannelAgentService;
import com.flash.paymentagent.api.conf.AliConfigParam;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;
import com.flash.paymentagent.api.dto.TradeStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "TP_PAYMENT_ORDER", consumerGroup = "CID_PAYMENT_CONSUMER")
public class PayConsumer implements RocketMQListener<MessageExt> {
    @Resource
    private IPayChannelAgentService iPayChannelAgentService;
    @Autowired
    private PayProducer payProducer;

    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("开始消费支付结果查询消息: {}", messageExt);
        // 取出消息内容
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        PaymentResponseDTO response = JSON.parseObject(body, PaymentResponseDTO.class);
        String outTradeNo = response.getOutTradeNo();
        String msg = response.getMsg();
        String param = String.valueOf(response.getContent());
        AliConfigParam aliConfigParam = JSON.parseObject(param, AliConfigParam.class);

        PaymentResponseDTO<Object> result = new PaymentResponseDTO<Object>();

        // 判断是支付宝还是微信
        if ("ALIPAY_WAP".equals(msg)) {
            // 查询支付宝支付结果
            result = iPayChannelAgentService.queryPayOrderByAli(aliConfigParam, outTradeNo);

            // 不管支付成功还是失败都需要发送支付结果消息
            log.info("交易中心处理支付结果通知, 支付代理发送消息: {}", result);
            payProducer.payResultNotice(result);
        }

        // 返回查询获得的支付状态
        if (TradeStatus.UNKNOWN.equals(result.getTradeState()) || TradeStatus.USERPAYING.equals(result.getTradeState())) {
            // 在支付状态未知或支付中, 抛出异常会重新消息此消息
            log.info("支付代理‐支付状态未知, 等待重试");
            throw new RuntimeException("支付状态未知, 等待重试");
        }
    }
}
