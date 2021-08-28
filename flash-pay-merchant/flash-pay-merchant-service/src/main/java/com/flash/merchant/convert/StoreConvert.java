package com.flash.merchant.convert;

import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface StoreConvert {
    StoreConvert INSTANCE = Mappers.getMapper(StoreConvert.class);

    StoreDto entity2dto(Store store);

    Store dto2entity(StoreDto storeDto);
}


