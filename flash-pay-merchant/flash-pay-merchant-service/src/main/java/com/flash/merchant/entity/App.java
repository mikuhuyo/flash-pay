package com.flash.merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@TableName("app")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    @TableField("APP_ID")
    private String appId;

    /**
     * 商店名称
     */
    @TableField("APP_NAME")
    private String appName;

    /**
     * 所属商户
     */
    @TableField("MERCHANT_ID")
    private Long merchantId;

    /**
     * 应用公钥(RSAWithSHA256)
     */
    @TableField("PUBLIC_KEY")
    private String publicKey;

    /**
     * 授权回调地址
     */
    @TableField("NOTIFY_URL")
    private String notifyUrl;


}
