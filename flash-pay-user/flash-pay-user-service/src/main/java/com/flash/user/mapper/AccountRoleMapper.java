package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flash.user.entity.AccountRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleMapper extends BaseMapper<AccountRole> {

    @Delete("delete from account_role r where  r.TENANT_ID=#{tenantId} and a.username=#{username} ")
    void deleteByUsernameInTenant(@Param("tenantId") Long tenantId, @Param("username") String username);

    /**
     * 获取用户在多租户下的角色
     *
     * @param username
     * @param ids
     * @return
     */
    @Select("<script> " +
            "SELECT r.id FROM " +
            "account_role ar, authorization_role r " +
            "WHERE r.`CODE` = ar.ROLE_CODE " +
            "AND r.`TENANT_ID` = ar.`TENANT_ID` " +
            "AND ar.username=#{username}  " +
            "and ar.TENANT_ID in <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach> " +
            "</script>")
    List<Long> selectRoleByUsernameInTenants(@Param("username") String username, @Param("ids") List<Long> ids);

    /**
     * 绑定角色
     *
     * @param username
     * @param tenantId
     * @param roleList
     */
    @Insert("<script>" +
            "INSERT INTO account_role(USERNAME,ROLE_CODE,TENANT_ID) VALUES " +
            "<foreach collection='roleList' item='item' separator=','>(#{username},#{item},#{tenantId})</foreach> " +
            "</script>")
    //@Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "ID")
    void insertAccountRole(@Param("username") String username, @Param("tenantId") Long tenantId, @Param("roleList") List<String> roleList);
}
