package com.flash.merchant.convert;

import com.flash.merchant.api.dto.AppDto;
import com.flash.merchant.entity.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface AppConvert {
    AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

    App dto2entity(AppDto appDto);

    AppDto entity2dto(App app);

    List<AppDto> entityList2dtoList(List<App> apps);
}
