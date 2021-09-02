package com.flash.transaction.convert;

import com.flash.transaction.api.dto.PayChannelParamDto;
import com.flash.transaction.entity.PayChannelParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface PayChannelParamConvert {

    PayChannelParamConvert INSTANCE = Mappers.getMapper(PayChannelParamConvert.class);

    PayChannelParamDto entity2dto(PayChannelParam entity);

    PayChannelParam dto2entity(PayChannelParamDto dto);

    List<PayChannelParamDto> entityList2dtoList(List<PayChannelParam> payChannelParams);

    List<PayChannelParam> dtoList2entityList(List<PayChannelParamDto> payChannelParamDtos);
}
