package com.flash.transaction.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeDto implements Serializable {

    private static final long serialVersionUID = -1871519087021645105L;

    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 商品标题
     */
    private String subject;
    /**
     * 订单描述
     */
    private String body;
}
