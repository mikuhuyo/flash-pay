package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flash.user.api.dto.authorization.PrivilegeTreeDTO;
import com.flash.user.entity.AuthorizationPrivilegeGroup;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorizationPrivilegeGroupMapper extends BaseMapper<AuthorizationPrivilegeGroup> {

    @Select("select * from authorization_privilege_group")
    List<PrivilegeTreeDTO> selectPrivilegeGroup();
}
