package com.flash.transaction.convert;

import com.flash.transaction.api.dto.PlatformChannelDto;
import com.flash.transaction.entity.PlatformChannel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface PlatformChannelConvert {
    PlatformChannelConvert INSTANCE = Mappers.getMapper(PlatformChannelConvert.class);

    List<PlatformChannelDto> entityList2dtoList(List<PlatformChannel> platformChannels);
}
