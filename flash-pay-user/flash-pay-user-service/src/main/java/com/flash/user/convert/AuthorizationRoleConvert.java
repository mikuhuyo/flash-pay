package com.flash.user.convert;

import com.flash.user.api.dto.authorization.RoleDTO;
import com.flash.user.entity.AuthorizationRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthorizationRoleConvert {

    AuthorizationRoleConvert INSTANCE = Mappers.getMapper(AuthorizationRoleConvert.class);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "tenantId", source = "tenantId")}
    )
    RoleDTO entity2dto(AuthorizationRole entity);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "tenantId", source = "tenantId")}
    )
    AuthorizationRole dto2entity(RoleDTO dto);

    List<RoleDTO> entitylist2dto(List<AuthorizationRole> authorizationRole);

    List<AuthorizationRole> dtolist2entity(List<RoleDTO> roleDTOS);

}
