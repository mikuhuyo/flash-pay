package com.flash.paymentagent.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一的支付状态回调
 *
 * @author yuelimin
 */
@Data
public class CommonPayStatusDTO implements Serializable {

    private static final long serialVersionUID = 1304872122980210633L;

    private String tradeNo;
    private String outTradeNo;
    private String tradeStatus;
    private String results;


    ///////////支付宝/////////////////
   /* 参数	参数名称	类型	必填	描述	范例
    notify_time	通知时间	Date	是	通知的发送时间. 格式为yyyy-MM-dd HH:mm:ss	2015-14-27 15:45:58
    notify_type	通知类型	String(64)	是	通知的类型	trade_status_sync
    notify_id	通知校验ID	String(128)	是	通知校验ID	ac05099524730693a8b330c5ecf72da9786
    app_id	开发者的app_id	String(32)	是	支付宝分配给开发者的应用Id	2014072300007148
    charset	编码格式	String(10)	是	编码格式, 如utf-8, gbk, gb2312等	utf-8
    version	接口版本	String(3)	是	调用的接口版本, 固定为: 1.0	1.0
    sign_type	签名类型	String(10)	是	商户生成签名字符串所使用的签名算法类型, 目前支持RSA2和RSA, 推荐使用RSA2	RSA2
    sign	签名	String(256)	是	请参考异步返回结果的验签	601510b7970e52cc63db0f44997cf70e
    trade_no	支付宝交易号	String(64)	是	支付宝交易凭证号	2013112011001004330000121536
    out_trade_no	商户订单号	String(64)	是	原支付请求的商户订单号	6823789339978248
    out_biz_no	商户业务号	String(64)	否	商户业务ID, 主要是退款通知中返回退款申请的流水号	HZRF001
    buyer_id	买家支付宝用户号	String(16)	否	买家支付宝账号对应的支付宝唯一用户号. 以2088开头的纯16位数字	2088102122524333
    buyer_logon_id	买家支付宝账号	String(100)	否	买家支付宝账号	159﹡﹡﹡﹡﹡﹡20
    seller_id	卖家支付宝用户号	String(30)	否	卖家支付宝用户号	2088101106499364
    seller_email	卖家支付宝账号	String(100)	否	卖家支付宝账号	zhu﹡﹡﹡@alitest.com
    trade_status	交易状态	String(32)	否	交易目前所处的状态, 见交易状态说明	TRADE_CLOSED
    total_amount	订单金额	Number(9,2)	否	本次交易支付的订单金额, 单位为人民币（元）	20
    receipt_amount	实收金额	Number(9,2)	否	商家在交易中实际收到的款项, 单位为元	15
    invoice_amount	开票金额	Number(9,2)	否	用户在交易中支付的可开发票的金额	10.00
    buyer_pay_amount	付款金额	Number(9,2)	否	用户在交易中支付的金额	13.88
    point_amount	集分宝金额	Number(9,2)	否	使用集分宝支付的金额	12.00
    refund_fee	总退款金额	Number(9,2)	否	退款通知中, 返回总退款金额, 单位为元, 支持两位小数	2.58
    subject	订单标题	String(256)	否	商品的标题/交易标题/订单标题/订单关键字等, 是请求时对应的参数, 原样通知回来	当面付交易
    body	商品描述	String(400)	否	该订单的备注, 描述, 明细等. 对应请求时的body参数, 原样通知回来	当面付交易内容
    gmt_create	交易创建时间	Date	否	该笔交易创建的时间. 格式为yyyy-MM-dd HH:mm:ss	2015-04-27 15:45:57
    gmt_payment	交易付款时间	Date	否	该笔交易的买家付款时间. 格式为yyyy-MM-dd HH:mm:ss	2015-04-27 15:45:57
    gmt_refund	交易退款时间	Date	否	该笔交易的退款时间. 格式为yyyy-MM-dd HH:mm:ss.S	2015-04-28 15:45:57.320
    gmt_close	交易结束时间	Date	否	该笔交易结束时间. 格式为yyyy-MM-dd HH:mm:ss	2015-04-29 15:45:57
    fund_bill_list	支付金额信息	String(512)	否	支付成功的各个渠道金额信息, 详见资金明细信息说明	[{"amount":"15.00","fundChannel":"ALIPAYACCOUNT"}]
    passback_params	回传参数	String(512)	否	公共回传参数, 如果请求时传递了该参数, 则返回给商户时会在异步通知时将该参数原样返回. 本参数必须进行UrlEncode之后才可以发送给支付宝	merchantBizType%3d3C%26merchantBizNo%3d2016010101111
    voucher_detail_list	优惠券信息	String	否	本交易支付时所使用的所有优惠券信息, 详见优惠券信息说明	[{"amount":"0.20","merchantContribute":"0.00","name":"一键创建券模板的券名称","otherContribute":"0.20","type":"ALIPAY_DISCOUNT_VOUCHER","memo":"学生卡8折优惠"]*/

    ///////////微信/////////////////
    /*
    字段名	变量名	必填	类型	示例值	描述
    返回状态码	return_code	是	String(16)	SUCCESS SUCCESS
    返回信息	return_msg	是	String(128)	OK	OK

    以下字段在return_code为SUCCESS的时候有返回
    字段名	变量名	必填	类型	示例值	描述
    公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID（企业号corpid即为此appId）
    商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
    设备号	device_info	否	String(32)	013467007045764	微信支付分配的终端设备号,
    随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串, 不长于32位
    签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名, 详见签名算法
    签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型, 目前支持HMAC-SHA256和MD5, 默认为MD5
    业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
    错误代码	err_code	否	String(32)	SYSTEMERROR	错误返回的信息描述
    错误代码描述	err_code_des	否	String(128)	系统错误	错误返回的信息描述
    用户标识	openid	是	String(128)	wxd930ea5d5a258f4f	用户在商户appid下的唯一标识
    是否关注公众账号	is_subscribe	是	String(1)	Y	用户是否关注公众账号, Y-关注, N-未关注
    交易类型	trade_type	是	String(16)	JSAPI	JSAPI, NATIVE, APP
    付款银行	bank_type	是	String(16)	CMC	银行类型, 采用字符串类型的银行标识, 银行类型见银行列表
    订单金额	total_fee	是	Int	100	订单总金额, 单位为分
    应结订单金额	settlement_total_fee	否	Int	100	应结订单金额=订单金额-非充值代金券金额, 应结订单金额<=订单金额.
    货币种类	fee_type	否	String(8)	CNY	货币类型, 符合ISO4217标准的三位字母代码, 默认人民币: CNY, 其他值列表详见货币类型
    现金支付金额	cash_fee	是	Int	100	现金支付金额订单现金支付金额, 详见支付金额
    现金支付货币类型	cash_fee_type	否	String(16)	CNY	货币类型, 符合ISO4217标准的三位字母代码, 默认人民币: CNY, 其他值列表详见货币类型
    总代金券金额	coupon_fee	否	Int	10	代金券金额<=订单金额, 订单金额-代金券金额=现金支付金额, 详见支付金额
    代金券使用数量	coupon_count	否	Int	1	代金券使用数量
    代金券类型	coupon_type_$n	否	String	CASH CASH--充值代金券 NO_CASH---非充值代金券 并且订单使用了免充值券后有返回（取值: CASH, NO_CASH）. $n为下标,从0开始编号, 举例: coupon_type_0
    代金券ID	coupon_id_$n	否	String(20)	10000	代金券ID,$n为下标, 从0开始编号
    单个代金券支付金额	coupon_fee_$n	否	Int	100	单个代金券支付金额,$n为下标, 从0开始编号
    微信支付订单号	transaction_id	是	String(32)	1217752501201407033233368018	微信支付订单号
    商户订单号	out_trade_no	是	String(32)	1212321211201407033568112322	商户系统内部订单号, 要求32个字符内, 只能是数字, 大小写字母_-|*@ , 且在同一个商户号下唯一.
    商家数据包	attach	否	String(128)	123456	商家数据包, 原样返回
    支付完成时间	time_end	是	String(14)	20141030133525	支付完成时间, 格式为yyyyMMddHHmmss, 如2009年12月25日9点10分10秒表示为20091225091010. 其他详见时间规则
    */

}
