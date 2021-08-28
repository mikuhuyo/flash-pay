package com.flash.merchant.convert;

import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.vo.MerchantDetailVo;
import com.flash.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface MerchantConvert {
    MerchantConvert INSTANCE = Mappers.getMapper(MerchantConvert.class);

    MerchantDto vo2dto(MerchantDetailVo merchantDetailVO);

    Merchant dto2entity(MerchantDto merchantDto);

    MerchantDto entity2dto(Merchant merchant);

    List<MerchantDto> entityList2dtoList(List<Merchant> merchantList);
}
