package com.flash.merchant.convert;

import com.flash.merchant.api.dto.StaffDto;
import com.flash.merchant.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Mapper
public interface StaffConvert {
    StaffConvert INSTANCE = Mappers.getMapper(StaffConvert.class);

    Staff dto2entity(StaffDto staffDto);

    StaffDto entity2dto(Staff staff);

    List<StaffDto> entityList2dtoList(List<Staff> staffs);
}

