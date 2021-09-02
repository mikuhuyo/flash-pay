package com.flash.paymentagent.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 支付实体对象
 * 根据支付宝接口协议
 *
 * @author yuelimin
 */
@Data
public class AlipayBCScanBean implements Serializable {

    private static final long serialVersionUID = 5700824942418363032L;

    // (必选) 商户网站订单系统中唯一订单号, 64个字符以内, 只能包含字母, 数字, 下划线.
    // 需保证商户系统端不能重复, 建议通过数据库sequence生成.
    private String outTradeNo;

    // (必选) 支付场景, 条码支付场景为bar_code
    private String scene;

    // (必选) 付款条码, 用户支付宝钱包手机app点击"付款"产生的付款条码
    private String authCode;

    // (可选) 销售产品码
    // 该字段不要传, 否则会报ACQ.ACCESS_FORBIDDEN	无权限使用接口
    // 未签约条码支付或者合同已到期
    private String productCode;

    // (可选) 订单标题, 粗略描述用户的支付目的.
    // 如"喜士多（浦东店）消费"
    private String subject;

    // (可选) 买家的支付宝用户id, 如果为空, 会从传入了码值信息中获取买家ID  2088202954065786
    private String buyerId;

    // (可选) 卖家支付宝账号ID, 用于支持一个签约账号下支持打款到不同的收款账号, (打款到sellerId对应的支付宝账号)
    // 如果该字段为空, 则默认为与支付宝签约的商户的PID, 也就是appid对应的PID
    private String sellerId;

    // (可选) 订单总金额, 单位为元, 精确到小数点后两位, 取值范围[0.01,100000000]
    //如果同时传入[可打折金额]和[不可打折金额], 该参数可以不用传入；
    //如果同时传入了[可打折金额], [不可打折金额], [订单总金额]三者, 则必须满足如下条件: [订单总金额]=[可打折金额]+[不可打折金额]
    private String totalAmount;

    // (可选) 标价币种, total_amount 对应的币种单位. 支持英镑: GBP, 港币: HKD, 美元: USD, 新加坡元: SGD, 日元: JPY, 加拿大元: CAD, 澳元: AUD, 欧元: EUR, 新西兰元: NZD, 韩元: KRW, 泰铢: THB, 瑞士法郎: CHF, 瑞典克朗: SEK, 丹麦克朗: DKK, 挪威克朗: NOK, 马来西亚林吉特: MYR, 印尼卢比: IDR, 菲律宾比索: PHP, 毛里求斯卢比: MUR, 以色列新谢克尔: ILS, 斯里兰卡卢比: LKR, 俄罗斯卢布: RUB, 阿联酋迪拉姆: AED, 捷克克朗: CZK, 南非兰特: ZAR, 人民币: CNY
    private String transCurrency;

    // (可选) 商户指定的结算币种, 支持英镑: GBP, 港币: HKD, 美元: USD, 新加坡元: SGD, 日元: JPY, 加拿大元: CAD, 澳元: AUD, 欧元: EUR, 新西兰元: NZD, 韩元: KRW, 泰铢: THB, 瑞士法郎: CHF, 瑞典克朗: SEK, 丹麦克朗: DKK, 挪威克朗: NOK, 马来西亚林吉特: MYR, 印尼卢比: IDR, 菲律宾比索: PHP, 毛里求斯卢比: MUR, 以色列新谢克尔: ILS, 斯里兰卡卢比: LKR, 俄罗斯卢布: RUB, 阿联酋迪拉姆: AED, 捷克克朗: CZK, 南非兰特: ZAR, 人民币: CNY
    private String settleCurrency;

    // (可选) 订单可打折金额, 此处单位为元, 精确到小数点后2位
    // 可以配合商家平台配置折扣活动, 如果订单部分商品参与打折, 可以将部分商品总价填写至此字段, 默认全部商品可打折
    // 如果该值未传入,但传入了[订单总金额],[不可打折金额] 则该值默认为[订单总金额]- [不可打折金额]
    private String discountableAmount;

    // (可选) 订单不可打折金额, 此处单位为元, 精确到小数点后2位, 可以配合商家平台配置折扣活动, 如果酒水不参与打折, 则将对应金额填写至此字段
    // 如果该值未传入,但传入了[订单总金额],[打折金额],则该值默认为[订单总金额]-[打折金额]
    private String undiscountableAmount;

    // (可选) 订单描述, 可以对交易或商品进行一个详细地描述, 比如填写"购买商品2件共15.00元"
    private String body;

    // (可选) 商品明细列表, 需填写购买商品详细信息,
    private List<GoodsDetail> goodsDetailList;

    // (可选) 商户操作员编号	yx_001
    private String operatorId;

    // (可选) 商户门店编号	NJ_001
    private String storeId;

    // (可选) 商户机具终端编号	NJ_T_001
    private String terminalId;

    // 业务扩展参数, 目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法), 详情请咨询支付宝技术支持
    //private ExtendParams extendParams;

    // (推荐使用, 相对时间) 该笔订单允许的最晚付款时间, 逾期将关闭交易. 取值范围: 1m～15d. m-分钟, h-小时, d-天, 1c-当天（1c-当天的情况下, 无论交易何时创建, 都在0点关闭）.  该参数数值不接受小数点,  如 1.5h, 可转换为 90m	90m
    private String timeoutExpress;

}
