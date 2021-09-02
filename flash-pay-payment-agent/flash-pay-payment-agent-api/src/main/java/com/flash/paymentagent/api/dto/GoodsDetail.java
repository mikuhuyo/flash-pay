package com.flash.paymentagent.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 */
@Data
public class GoodsDetail implements Serializable {

    private static final long serialVersionUID = -3799303671243086119L;

    /**
     * 支付宝定义的统一商品编号
     */
    private String alipayGoodsId;

    /**
     * 商品描述信息
     */
    private String body;

    /**
     * 商品类目树, 从商品类目根节点到叶子节点的类目id组成, 类目id值使用|分割
     */
    private String categoriesTree;

    /**
     * 商品类目
     */
    private String goodsCategory;

    /**
     * 商品的编号
     */
    private String goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价, 单位为元
     */
    private String price;

    /**
     * 商品数量
     */
    private Long quantity;

    /**
     * 商品的展示地址
     */
    private String showUrl;
}
