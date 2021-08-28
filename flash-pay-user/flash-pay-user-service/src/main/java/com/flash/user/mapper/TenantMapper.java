package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.user.api.dto.tenant.AccountRoleDTO;
import com.flash.user.api.dto.tenant.AccountRoleQueryDTO;
import com.flash.user.api.dto.tenant.TenantDTO;
import com.flash.user.api.dto.tenant.TenantQueryDTO;
import com.flash.user.entity.Tenant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    //向租户-账号中间表插入数据
    @Insert("insert into tenant_account(TENANT_ID,ACCOUNT_ID,IS_ADMIN) values(#{id1},#{id2},#{isAdmin})")
    void insertTenantAccount(@Param("id1") Long id1, @Param("id2") Long id2, @Param("isAdmin") Boolean isAdmin);

    @Select("select t.* from tenant t \n" +
            "left join tenant_account ta ON t.ID=ta.TENANT_ID \n" +
            "left join account a on a.ID =ta.ACCOUNT_ID \n" +
            "where ta.ACCOUNT_ID=#{id}")
    List<TenantDTO> selectAccountInTenant(@Param("id") Long id);

    /**
     * 分页检索租户
     *
     * @param page
     * @param tenantQuery
     * @param sortBy
     * @param order
     * @return
     */
    @Select("<script>"
            + "select * from tenant t "
            + "<where>"
            + "<if test=\"tenantQuery.name != null and tenantQuery.name!=''\"> "
            + "and t.NAME= #{tenantQuery.name} "
            + "</if>"
            + "<if test=\"tenantQuery.tenantTypeCode != null and tenantQuery.tenantTypeCode!=''\"> "
            + "and t.TENANT_TYPE_CODE= #{tenantQuery.tenantTypeCode} "
            + "</if>"
            + "</where>"
            + " ORDER BY #{sortBy} #{order} "
            + "</script>")
    List<TenantDTO> selectTenantByPage(@Param("page") Page<TenantDTO> page, @Param("tenantQuery") TenantQueryDTO tenantQuery,
                                       @Param("sortBy") String sortBy, @Param("order") String order);

    @Select("select ar.USERNAME as username,ar.TENANT_ID as tenantId,r.`NAME` as roleName from account_role ar " +
            "join account a on ar.USERNAME = a.USERNAME " +
            "join authorization_role r on ar.TENANT_ID= r.TENANT_ID and ar.ROLE_CODE=r.`CODE` " +
            "where a.USERNAME=#{query.username} " +
            "and r.TENANT_ID=#{query.tenantId}")
    List<AccountRoleQueryDTO> queryAdministratorByPage(@Param("page") Page<AccountRoleQueryDTO> page, @Param("query") AccountRoleDTO query);
}
