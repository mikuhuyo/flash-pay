package com.flash.user.convert;

import com.flash.user.api.dto.tenant.AccountRoleDTO;
import com.flash.user.entity.AccountRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountRoleConvert {

    AccountRoleConvert INSTANCE = Mappers.getMapper(AccountRoleConvert.class);

    AccountRoleDTO entity2dto(AccountRole entity);

    AccountRole dto2entity(AccountRoleDTO dto);

    List<AccountRoleDTO> listentity2dto(List<AccountRole> app);
}
