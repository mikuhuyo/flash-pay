package com.flash.user.api;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.PageVO;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.resource.ApplicationQueryParams;
import com.flash.user.api.dto.resource.ResourceDTO;

import java.util.List;
import java.util.Map;

/**
 * 资源服务
 */
public interface ResourceService {
    /**
     * 创建应用
     * 会关联创建uaa服务中的接入客户端, 其中code为clientid
     *
     * @param application
     */
    void createApplication(ApplicationDTO application) throws BusinessException;

    /**
     * 修改应用
     * 仅仅可以修改名称
     *
     * @param application
     */
    void modifyApplication(ApplicationDTO application) throws BusinessException;

    /**
     * 删除应用
     * 关联删除uaa服务中的接入客户端, 若应用下有资源, 禁止删除
     *
     * @param applicationCode
     */
    void removeApplication(String applicationCode) throws BusinessException;

    /**
     * 根据应用编码查找应用
     *
     * @param applicationCode
     * @return
     */
    ApplicationDTO queryApplication(String applicationCode) throws BusinessException;

    /**
     * 条件分页查找应用
     *
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageVO<ApplicationDTO> pageApplicationByConditions(ApplicationQueryParams query,
                                                       Integer pageNo, Integer pageSize) throws BusinessException;

    /**
     * 根据权限加载指定应用的资源
     *
     * @param privileageCodes
     * @param applicationCode
     * @return
     */
    List<ResourceDTO> loadResources(List<String> privileageCodes, String applicationCode) throws BusinessException;


    /**
     * 获取多个租户下权限所对应的的资源信息, 并按应用拆分
     *
     * @param tenantAuthorizationInfoMap 多个租户下的权限信息, key为租户id  value为租户下的角色权限信息
     * @return 多个租户下的资源信息, key为租户id  value为租户下多个应用的资源信息
     * 如: {租户A:[{应用1权限信息},{应用2权限信息}],租户B:[{},{}...]}
     */
    Map<Long, List<ResourceDTO>> loadResources(Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap) throws BusinessException;


}
