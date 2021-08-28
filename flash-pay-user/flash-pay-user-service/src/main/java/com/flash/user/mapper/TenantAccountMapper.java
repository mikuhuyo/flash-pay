package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flash.user.api.dto.tenant.AccountRoleQueryDTO;
import com.flash.user.entity.TenantAccount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantAccountMapper extends BaseMapper<TenantAccount> {

    @Select("select count(*) from tenant_account where ACCOUNT_ID IN(select id from account where USERNAME=#{username})")
    int selectTenantByUsernameInAccount(@Param("username") String username);

    @Delete("delete from tenant_account where TENANT_ID=#{tenantId} and ACCOUNT_ID=#{accountId} ")
    void deleteAccountInTenant(@Param("tenantId") Long tenantId, @Param("accountId") Long accountId);

    @Select("select ar.USERNAME as username,r.`NAME` as roleName,r.TENANT_ID as tenantId, ar.ID as id " +
            "from account_role ar " +
            "join authorization_role r on ar.ROLE_CODE=r.`CODE` " +
            "and ar.TENANT_ID=r.TENANT_ID " +
            "where r.TENANT_ID=#{tenantId} " +
            "and r.`CODE`=#{roleCode} " +
            "and ar.USERNAME=#{username}")
    AccountRoleQueryDTO selectAccountRole(@Param("username") String username, @Param("roleCode") String roleCode, @Param("tenantId") Long tenantId);
}
