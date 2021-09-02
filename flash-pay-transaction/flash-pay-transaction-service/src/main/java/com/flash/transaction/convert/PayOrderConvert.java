package com.flash.transaction.convert;

import com.flash.transaction.api.dto.PayOrderDto;
import com.flash.transaction.api.vo.OrderConfirmVo;
import com.flash.transaction.entity.PayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author yuelimin
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDto entity2dto(PayOrder entity);

    PayOrder dto2entity(PayOrderDto dto);

    /**
     * 忽略totalAmount, 不做映射
     *
     * @param vo
     * @return
     */
    @Mapping(target = "totalAmount", ignore = true)
    PayOrderDto vo2dto(OrderConfirmVo vo);
}
