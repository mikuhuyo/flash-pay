package com.flash.user.api;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.PageVO;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.authorization.PrivilegeDTO;
import com.flash.user.api.dto.authorization.PrivilegeTreeDTO;
import com.flash.user.api.dto.authorization.RoleDTO;
import com.flash.user.api.dto.tenant.AccountRoleDTO;

import java.util.List;
import java.util.Map;

/**
 * 授权服务: 负责 用户-角色关系, 角色, 权限, 等信息维护
 * <p>
 * 权限组, 权限为手工维护
 * <p>
 * 租户内角色数量, 租户内账号数量, 应用数量可用套餐限制
 */
public interface AuthorizationService {

    /**
     * 判断账号是否绑定了某些角色
     *
     * @param username
     * @param tenantId
     * @param roleCodes
     * @return
     * @throws BusinessException
     */
    List<AccountRoleDTO> queryAccountBindRole(String username, Long tenantId, String[] roleCodes) throws BusinessException;

    /**
     * 根据租户内的账号查询绑定的角色
     *
     * @param username
     * @param tenantId
     * @return
     */
    List<AccountRoleDTO> queryAccountRole(String username, Long tenantId);

    /**
     * 根据租户内的账号查询绑定的角色列表
     *
     * @param username
     * @param tenantId
     * @return
     */
    List<RoleDTO> queryRolesByUsername(String username, Long tenantId);

    /**
     * 授权, 获取某用户在多个租户下的权限信息
     *
     * @param username  用户名
     * @param tenantIds 租户id列表
     * @return key为租户id  value为租户下的角色权限信息
     */
    Map<Long, AuthorizationInfoDTO> authorize(String username, Long[] tenantIds) throws BusinessException;


    /**
     * 查找某租户下, 多个角色的权限信息集合
     *
     * @param roleCodes
     * @return List<PrivilegeTreeDTO>
     */
    List<PrivilegeDTO> queryPrivilege(Long tenantId, String[] roleCodes) throws BusinessException;

    /**
     * 获取权限组下所有权限
     *
     * @param privilegeGroupId 权限组ID
     * @return
     */
    List<PrivilegeDTO> queryPrivilegeByGroupId(Long privilegeGroupId) throws BusinessException;


    /**
     * 查找某租户下, 多个角色的权限信息集合, 并根据权限组组装成为权限树
     *
     * @param tenantId
     * @param roleCodes 角色编码列表, 为null时代表所有权限
     * @return
     */
    PrivilegeTreeDTO queryPrivilegeTree(Long tenantId, String[] roleCodes) throws BusinessException;


    //////////////////////////////////////////////////角色, 权限///////////////////////////////////////////////////

    /**
     * 创建租户内角色(不包含权限)
     *
     * @param tenantId
     * @param role
     * @throws BusinessException
     */
    void createRole(Long tenantId, RoleDTO role) throws BusinessException;

    /**
     * 删除租户内角色, 如果有账号绑定该角色, 禁止删除
     *
     * @param tenantId
     * @param roleCode
     */
    void removeRole(Long tenantId, String roleCode) throws BusinessException;

    /**
     * 删除租户内角色
     *
     * @param id 角色id
     */
    void removeRole(Long id) throws BusinessException;

    /**
     * 修改租户内角色(不包含权限)
     *
     * @param role
     */
    void modifyRole(RoleDTO role) throws BusinessException;


    /**
     * 角色设置权限(清除+设置)
     *
     * @param tenantId
     * @param roleCode
     * @param privilegeCodes
     */
    void roleBindPrivilege(Long tenantId, String roleCode, String[] privilegeCodes) throws BusinessException;

    /**
     * 查询某租户下角色(不分页, 不包含权限)
     *
     * @param tenantId
     * @return
     */
    List<RoleDTO> queryRole(Long tenantId);

    /**
     * 根据roleCode获取角色(不包含权限)
     *
     * @param tenantId
     * @param roleCodes
     * @return
     */
    List<RoleDTO> queryRole(Long tenantId, String... roleCodes) throws BusinessException;


    /**
     * 获取租户内的某个角色信息(包含权限信息)
     *
     * @param tenantId
     * @param roleCode
     * @return
     */
    RoleDTO queryTenantRole(Long tenantId, String roleCode) throws BusinessException;


    /**
     * 绑定角色
     *
     * @param username  用户名
     * @param tenantId  租户id
     * @param roleCodes 角色列表
     * @return
     */
    void bindAccountRole(String username, Long tenantId, String[] roleCodes) throws BusinessException;

    /**
     * 解绑角色
     *
     * @param username  用户名
     * @param tenantId  租户id
     * @param roleCodes 角色列表
     * @return
     */
    void unbindAccountRole(String username, Long tenantId, String[] roleCodes) throws BusinessException;

    /**
     * 分页查询角色
     *
     * @param roleDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageVO<RoleDTO> queryRoleByPage(RoleDTO roleDTO, Integer pageNo, Integer pageSize);
}
