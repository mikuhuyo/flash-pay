package com.flash.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.user.api.AuthorizationService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.authorization.PrivilegeDTO;
import com.flash.user.api.dto.authorization.PrivilegeTreeDTO;
import com.flash.user.api.dto.authorization.RoleDTO;
import com.flash.user.api.dto.tenant.AccountRoleDTO;
import com.flash.user.api.dto.tenant.TenRolePrivilegeDTO;
import com.flash.user.convert.AccountRoleConvert;
import com.flash.user.convert.AuthorizationPrivilegeConvert;
import com.flash.user.convert.AuthorizationRoleConvert;
import com.flash.user.entity.*;
import com.flash.user.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private AuthorizationRoleMapper roleMapper;
    @Autowired
    private AuthorizationPrivilegeMapper privilegeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountRoleMapper accountRoleMapper;
    @Autowired
    private AuthorizationRolePrivilegeMapper rolePrivilegeMapper;
    @Autowired
    private AuthorizationPrivilegeGroupMapper groupMapper;

    @Override
    public List<AccountRoleDTO> queryAccountBindRole(String username, Long tenantId, String[] roleCodes) {
        List<String> roleList = new ArrayList<>(Arrays.asList(roleCodes));
        List<AccountRole> accountRoles = accountRoleMapper.selectList(new QueryWrapper<AccountRole>().lambda()
                .eq(AccountRole::getUsername, username).eq(AccountRole::getTenantId, tenantId)
                .in(AccountRole::getRoleCode, roleList));
        return AccountRoleConvert.INSTANCE.listentity2dto(accountRoles);
    }

    @Override
    public List<AccountRoleDTO> queryAccountRole(String username, Long tenantId) {
        List<AccountRole> accountRoles = accountRoleMapper.selectList(new QueryWrapper<AccountRole>().lambda()
                .eq(AccountRole::getUsername, username)
                .eq(AccountRole::getTenantId, tenantId));
        return AccountRoleConvert.INSTANCE.listentity2dto(accountRoles);
    }

    @Override
    public List<RoleDTO> queryRolesByUsername(String username, Long tenantId) {
        // 获取员工用户名, 即账号的用户名
        List<AccountRoleDTO> list = queryAccountRole(username, tenantId);
        List<RoleDTO> roleDTOS = null;
        if (!list.isEmpty()) {
            List<String> codes = new ArrayList<>();
            list.forEach(ar -> {
                String roleCode = ar.getRoleCode();
                codes.add(roleCode);
            });
            String[] c = codes.toArray(new String[codes.size()]);
            roleDTOS = queryRole(tenantId, c);
        }
        return roleDTOS;
    }

    /**
     * 授权, 获取某用户在多个租户下的权限信息
     *
     * @param username  用户名
     * @param tenantIds 租户id列表
     * @return key为租户id  value为租户下的角色权限信息
     */
    @Override
    public Map<Long, AuthorizationInfoDTO> authorize(String username, Long[] tenantIds) {
        List<Long> ids = Arrays.asList(tenantIds);
        List<Long> roleIds = accountRoleMapper.selectRoleByUsernameInTenants(username, ids);
        List<TenRolePrivilegeDTO> list = privilegeMapper.selectPrivilegeRoleInTenant(roleIds);
        if (list == null) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        Map<Long, AuthorizationInfoDTO> map = new HashMap<>();
        for (TenRolePrivilegeDTO dto : list) {
            if (!map.containsKey(dto.getTenantId())) {
                map.put(dto.getTenantId(), new AuthorizationInfoDTO());
            }
            AuthorizationInfoDTO info = map.get(dto.getTenantId());
            if (info.getRolePrivilegeMap().containsKey(dto.getRoleCode())) {
                info.getRolePrivilegeMap().get(dto.getRoleCode()).add(dto.getPrivilegeCode());
            } else {
                List<String> prviList = new ArrayList<>();
                prviList.add(dto.getPrivilegeCode());
                info.getRolePrivilegeMap().put(dto.getRoleCode(), prviList);
            }
        }
        return map;
    }

    /**
     * 查找某租户下, 多个角色的权限信息集合
     *
     * @param roleCodes
     * @return List<PrivilegeTreeDTO>
     */
    @Override
    public List<PrivilegeDTO> queryPrivilege(Long tenantId, String[] roleCodes) {
        //先获取某租户下的角色
        List<AuthorizationRole> roles = roleMapper.selectList(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId)
                .in(AuthorizationRole::getCode, roleCodes));
        //获取多个角色的权限权限集合
        List<AuthorizationPrivilege> privileges = new ArrayList<>();
        if (!roles.isEmpty()) {
            List<Long> roleIds = roles.stream().map(AuthorizationRole::getId).collect(Collectors.toList());
            privileges = privilegeMapper.selectPrivilegeByRole(roleIds);
        }
        List<PrivilegeDTO> privilegeDTOS = AuthorizationPrivilegeConvert.INSTANCE.entitylist2dto(privileges);
        return privilegeDTOS;
    }

    /**
     * 获取权限组下所有权限
     *
     * @param privilegeGroupId 权限组ID
     * @return
     */
    @Override
    public List<PrivilegeDTO> queryPrivilegeByGroupId(Long privilegeGroupId) {
        List<AuthorizationPrivilege> privilegeList = privilegeMapper.selectList(new QueryWrapper<AuthorizationPrivilege>().lambda()
                .eq(AuthorizationPrivilege::getPrivilegeGroupId, privilegeGroupId));
        List<PrivilegeDTO> privilegeDTOS = AuthorizationPrivilegeConvert.INSTANCE.entitylist2dto(privilegeList);
        return privilegeDTOS;
    }

    /**
     * 查找某租户下, 多个角色的权限信息集合, 并根据权限组组装成为权限树
     *
     * @param tenantId
     * @param roleCodes 角色编码列表, 为null时代表所有权限
     * @return
     */
    @Override
    public PrivilegeTreeDTO queryPrivilegeTree(Long tenantId, String[] roleCodes) {
        //1.获取租户下角色对应的权限集合
        List<PrivilegeDTO> pList = queryPrivilege(tenantId, roleCodes);
        HashSet<PrivilegeDTO> h = new HashSet<PrivilegeDTO>(pList);
        pList.clear();
        pList.addAll(h);
        //2.获取所有权限组
        List<AuthorizationPrivilegeGroup> groupList = groupMapper.selectList(null);

        Map<String, PrivilegeTreeDTO> groupsMap = new HashMap<>();
        String topId = "top_1";
        PrivilegeTreeDTO topTree = new PrivilegeTreeDTO();
        topTree.setId(topId);
        topTree.setParentId(null);
        topTree.setName(null);
        topTree.setStatus(0);

        for (AuthorizationPrivilegeGroup g : groupList) {
            if (g.getParentId() == null) {
                PrivilegeTreeDTO child = new PrivilegeTreeDTO();
                child.setId(String.valueOf(g.getId()));
                child.setParentId(topId);
                child.setName(g.getName());
                child.setGroup(true);
                child.setStatus(1);
                topTree.getChildren().add(child);

                privGroupTree(child, groupList, groupsMap);
            }
        }

        for (PrivilegeDTO priv : pList) {

            String privGroupId = String.valueOf(priv.getPrivilegeGroupId());
            PrivilegeTreeDTO pGroupTreeDto = groupsMap.get(privGroupId);
            if (pGroupTreeDto != null) {

                PrivilegeTreeDTO pTreeDto = new PrivilegeTreeDTO();
                pTreeDto.setGroup(false);
                pTreeDto.setName(priv.getName());
                pTreeDto.setId(priv.getCode());
                pTreeDto.setParentId(privGroupId);
                pTreeDto.setStatus(1);

                pGroupTreeDto.getChildren().add(pTreeDto);
            }
        }
        return topTree;
    }


    private void privGroupTree(
            PrivilegeTreeDTO currChild, List<AuthorizationPrivilegeGroup> groupList, Map<String, PrivilegeTreeDTO> groupsMap) {
        if (!groupsMap.containsKey(currChild.getId())) {
            groupsMap.put(currChild.getId(), currChild);
        }
        for (AuthorizationPrivilegeGroup ccGroup : groupList) {
            if (String.valueOf(ccGroup.getParentId()).equals(currChild.getId())) {

                PrivilegeTreeDTO tmp = new PrivilegeTreeDTO();
                tmp.setId(String.valueOf(ccGroup.getId()));
                tmp.setParentId(currChild.getId());
                tmp.setName(ccGroup.getName());
                tmp.setGroup(true);
                tmp.setStatus(1);
                currChild.getChildren().add(tmp);

                //是否是多余的
                if (!groupsMap.containsKey(tmp.getId())) {
                    groupsMap.put(tmp.getId(), tmp);
                }
                privGroupTree(tmp, groupList, groupsMap);
            }
        }
    }

/////////////////////////////////////////////角色, 权限/////////////////////////////////////////////

    /**
     * 创建租户内角色(不包含权限)
     *
     * @param tenantId
     * @param role
     */
    @Override
    public void createRole(Long tenantId, RoleDTO role) {
        //此处需要校验 同一租户下角色code不能相同
        if (role == null || StringUtils.isBlank(role.getCode())) {
            throw new BusinessException(CommonErrorCode.E_110003);
        }
        String code = role.getCode();
        if (isExistRoleCode(tenantId, code)) {
            throw new BusinessException(CommonErrorCode.E_110002);
        }
        AuthorizationRole entity = AuthorizationRoleConvert.INSTANCE.dto2entity(role);
        roleMapper.insert(entity);
    }

    /**
     * 判断角色编码是否在某租户下已存在
     *
     * @param tenantId
     * @param roleCode
     * @return
     */
    private boolean isExistRoleCode(Long tenantId, String roleCode) {
        int i = roleMapper.selectRoleCodeInTenant(tenantId, roleCode);
        return i > 0;
    }

    /**
     * 删除租户内角色, 如果有账号绑定该角色, 禁止删除
     *
     * @param tenantId
     * @param roleCode
     */
    @Override
    public void removeRole(Long tenantId, String roleCode) {
        boolean b = accountMapper.selectAccountByRole(tenantId, roleCode);
        if (b) {
            throw new BusinessException(CommonErrorCode.E_110004);
        }
        AuthorizationRole role = roleMapper.selectOne(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId).eq(AuthorizationRole::getCode, roleCode));
        if (role != null && role.getId() != null) {
            removeRole(role.getId());
        }

    }

    /**
     * 删除租户内角色
     *
     * @param id 角色id
     */
    @Override
    public void removeRole(Long id) {

        roleMapper.deleteById(id);
    }

    /**
     * 修改租户内角色(不包含权限)
     *
     * @param role
     */
    @Override
    public void modifyRole(RoleDTO role) {
        //UpdateWrapper<AuthorizationRole> uw = new UpdateWrapper<>();
        //uw.lambda().eq(AuthorizationRole::getId,role.getId()).set(AuthorizationRole::getName,role.getName());
        //roleMapper.update(null,uw);
        AuthorizationRole entity = AuthorizationRoleConvert.INSTANCE.dto2entity(role);
        roleMapper.updateById(entity);
    }

    /**
     * 角色设置权限(清除+设置)
     *
     * @param tenantId       租户id
     * @param roleCode       角色code
     * @param privilegeCodes 该角色对应的权限集合
     */
    @Override
    public void roleBindPrivilege(Long tenantId, String roleCode, String[] privilegeCodes) {

        //1.获取租户内的某个角色信息(包含权限信息)
        RoleDTO roleDTOS = queryTenantRole(tenantId, roleCode);
        if (privilegeCodes == null || privilegeCodes.length == 0) {
            throw new BusinessException(CommonErrorCode.E_110005);
        }
        //2.根据权限code获取权限实体集合
        List<AuthorizationPrivilege> privileges = privilegeMapper.selectList(new QueryWrapper<AuthorizationPrivilege>().lambda()
                .in(AuthorizationPrivilege::getCode, privilegeCodes));
        if (privileges.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_110005);
        }

        //组装权限id集合
        List<Long> pids = new ArrayList<>();
        for (AuthorizationPrivilege p : privileges) {
            pids.add(p.getId());
        }

        //3.删除角色已关联的权限信息
        if (roleDTOS != null && roleDTOS.getId() != null) {
            Long roleId = roleDTOS.getId();
            //若角色已关联权限, 清除
            rolePrivilegeMapper.delete(new QueryWrapper<AuthorizationRolePrivilege>().lambda()
                    .eq(AuthorizationRolePrivilege::getRoleId, roleId));
        }

        //4.将角色和权限进行关联操作authorization_role_privilege表
        rolePrivilegeMapper.insertRolePrivilege(roleDTOS.getId(), pids);
    }

    /**
     * 查询某租户下角色(不分页, 不包含权限)
     *
     * @param tenantId
     * @return
     */
    @Override
    public List<RoleDTO> queryRole(Long tenantId) {
        QueryWrapper<AuthorizationRole> qw = new QueryWrapper<>();
        qw.lambda().eq(AuthorizationRole::getTenantId, tenantId);
        List<AuthorizationRole> authorizationRoles = roleMapper.selectList(qw);
        List<RoleDTO> roleDTOS = AuthorizationRoleConvert.INSTANCE.entitylist2dto(authorizationRoles);
        return roleDTOS;
    }

    /**
     * 根据roleCode获取角色(不包含权限)
     *
     * @param tenantId
     * @param roleCodes
     * @return
     */
    @Override
    public List<RoleDTO> queryRole(Long tenantId, String... roleCodes) {
        List<String> codes = Arrays.asList(roleCodes);
        List<AuthorizationRole> authorizationRoles = roleMapper.selectList(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId)
                .in(AuthorizationRole::getCode, codes));
        List<RoleDTO> roleDTOS = AuthorizationRoleConvert.INSTANCE.entitylist2dto(authorizationRoles);
        return roleDTOS;
    }


    /**
     * 获取租户内的某个角色信息(包含权限信息)
     *
     * @param tenantId
     * @param roleCode
     * @return
     */
    @Override
    public RoleDTO queryTenantRole(Long tenantId, String roleCode) {
        AuthorizationRole role = roleMapper.selectOne(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId).eq(AuthorizationRole::getCode, roleCode));
        if (role == null) {
            throw new BusinessException(CommonErrorCode.E_110003);
        }
        Long id = role.getId();
        RoleDTO roleDTO = AuthorizationRoleConvert.INSTANCE.entity2dto(role);
        roleDTO.setPrivilegeCodes(roleMapper.selectPrivilegeByRole(id));

        return roleDTO;
    }

    /**
     * 绑定角色
     *
     * @param username  用户名
     * @param tenantId  租户id
     * @param roleCodes 角色列表
     * @return
     */
    @Override
    public void bindAccountRole(String username, Long tenantId, String[] roleCodes) {
        //批量插入
        List<String> roleList = new ArrayList<>(Arrays.asList(roleCodes));
        accountRoleMapper.insertAccountRole(username, tenantId, roleList);
    }

    /**
     * 解绑角色
     *
     * @param username  用户名
     * @param tenantId  租户id
     * @param roleCodes 角色列表
     * @return
     */
    @Override
    public void unbindAccountRole(String username, Long tenantId, String[] roleCodes) {
        List<String> roleList = new ArrayList<>(Arrays.asList(roleCodes));
        //根据查询到的角色id清除掉对应租户
        List<AuthorizationRole> roles = roleMapper.selectList(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId)
                .in(AuthorizationRole::getCode, roleList));
        if (roles.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        List<Long> roleIds = new ArrayList<>();
        for (AuthorizationRole role : roles) {
            Long id = role.getId();
            roleIds.add(id);
        }
        roleMapper.update(null, new UpdateWrapper<AuthorizationRole>().lambda()
                .in(AuthorizationRole::getId, roleIds).set(AuthorizationRole::getTenantId, null));
        //根据账号-角色表id清除关系
        List<AccountRole> accountRoles = accountRoleMapper.selectList(new QueryWrapper<AccountRole>().lambda()
                .eq(AccountRole::getUsername, username).eq(AccountRole::getTenantId, tenantId)
                .in(AccountRole::getId, roleList));
        if (accountRoles.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        List<Long> ids = new ArrayList<>();
        for (AccountRole accountRole : accountRoles) {
            Long id = accountRole.getId();
            ids.add(id);
        }
        accountRoleMapper.update(null, new UpdateWrapper<AccountRole>().lambda()
                .in(AccountRole::getId, ids).set(AccountRole::getRoleCode, null));

    }

    @Override
    public PageVO<RoleDTO> queryRoleByPage(RoleDTO roleDTO, Integer pageNo, Integer pageSize) {
        return buildRoleQuery(roleDTO, pageNo, pageSize);
    }

    private PageVO<RoleDTO> buildRoleQuery(RoleDTO roleDTO, Integer pageNo, Integer pageSize) {
        // 创建分页
        Page<AuthorizationRole> page = new Page<>(pageNo, pageSize);
        // 构造查询条件
        QueryWrapper<AuthorizationRole> qw = new QueryWrapper();
        if (null != roleDTO && null != roleDTO.getTenantId()) {
            qw.lambda().eq(AuthorizationRole::getTenantId, roleDTO.getTenantId());
        }
        if (null != roleDTO && null != roleDTO.getName()) {
            qw.lambda().eq(AuthorizationRole::getName, roleDTO.getName());
        }
        // 执行查询
        IPage<AuthorizationRole> roleIPage = roleMapper.selectPage(page, qw);
        // entity List转DTO List
        List<RoleDTO> roleList = AuthorizationRoleConvert.INSTANCE.entitylist2dto(roleIPage.getRecords());
        // 封装结果集
        return new PageVO<>(roleList, roleIPage.getTotal(), pageNo, pageSize);
    }

}
