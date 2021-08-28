package com.flash.user.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.user.api.ResourceService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.resource.ApplicationQueryParams;
import com.flash.user.api.dto.resource.ResourceDTO;
import com.flash.user.convert.ResourceApplicationConvert;
import com.flash.user.entity.ResourceApplication;
import com.flash.user.entity.ResourceButton;
import com.flash.user.entity.ResourceMenu;
import com.flash.user.mapper.ResourceApplicationMapper;
import com.flash.user.mapper.ResourceButtonMapper;
import com.flash.user.mapper.ResourceMenuMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceApplicationMapper applicationMapper;
    @Autowired
    private ResourceMenuMapper menuMapper;
    @Autowired
    private ResourceButtonMapper buttonMapper;
    //@Reference
    //private ClientDetailsService clientDetailsService;

    /**
     * 创建应用
     *
     * @param application
     */
    @Override
    public void createApplication(ApplicationDTO application) {
        ResourceApplication entity = ResourceApplicationConvert.INSTANCE.dto2entity(application);
        applicationMapper.insert(entity);
    }

    /**
     * 修改应用
     * 仅仅可以修改名称
     *
     * @param application
     */
    @Override
    public void modifyApplication(ApplicationDTO application) {
        Assert.notNull(application, "对象不能为空");
        ResourceApplication entity = ResourceApplicationConvert.INSTANCE.dto2entity(application);
        applicationMapper.updateById(entity);
    }

    /**
     * 删除应用
     * 关联删除uaa服务中的接入客户端, 若应用下有资源, 禁止删除
     *
     * @param applicationCode
     */
    @Override
    public void removeApplication(String applicationCode) {
        //查询应用下是否有资源
        //TODO:按钮也是资源, 也需要判断
        int i = menuMapper.selectResourceByapplicationCode(applicationCode);
        Assert.isTrue(i > 0, "应用下有关联资源, 不能删除");
        applicationMapper.delete(new QueryWrapper<ResourceApplication>().lambda().eq(ResourceApplication::getCode, applicationCode));

        //关联删除uaa中客户端
        //clientDetailsService.delete(applicationCode);
    }

    /**
     * 根据应用编码查找应用
     *
     * @param applicationCode
     * @return
     */
    @Override
    public ApplicationDTO queryApplication(String applicationCode) {
        ResourceApplication application = applicationMapper.selectOne(new QueryWrapper<ResourceApplication>().lambda().eq(ResourceApplication::getCode, applicationCode));
        ApplicationDTO applicationDTO = ResourceApplicationConvert.INSTANCE.entity2dto(application);
        return applicationDTO;
    }

    /**
     * 条件分页查找应用
     *
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageVO<ApplicationDTO> pageApplicationByConditions(ApplicationQueryParams query, Integer pageNo, Integer pageSize) {

        Page<ApplicationDTO> page = new Page<>(pageNo, pageSize);// 当前页, 总条数 构造 page 对象
        List<ApplicationDTO> apps = applicationMapper.selectAppByPage(page, query);
        return new PageVO<>(apps, page.getTotal(), pageNo, pageSize);
    }

    /**
     * 根据权限加载指定应用的资源
     *
     * @param privilegeCodes
     * @param applicationCode
     * @return
     */
    @Override
    public List<ResourceDTO> loadResources(List<String> privilegeCodes, String applicationCode) {
        //1.获取应用的全部信息
        ResourceApplication app = applicationMapper.selectOne(new QueryWrapper<ResourceApplication>()
                .lambda().eq(ResourceApplication::getCode, applicationCode));
        //2.获取应用下的菜单资源
        List<ResourceMenu> resourceMenus = menuMapper.selectList(new QueryWrapper<ResourceMenu>().lambda()
                .eq(ResourceMenu::getApplicationCode, applicationCode).in(ResourceMenu::getPrivilegeCode, privilegeCodes));
        String menus = JSON.toJSONString(resourceMenus);
        //3.获取应用下的按钮资源
        List<ResourceButton> resourceButtons = buttonMapper.selectList(new QueryWrapper<ResourceButton>().
                lambda().eq(ResourceButton::getApplicationCode, applicationCode).in(ResourceButton::getPrivilegeCode, privilegeCodes));
        String buttons = JSON.toJSONString(resourceButtons);
        //4.组装应用的资源数据
        Map<String, Object> appRes = new HashMap<>();
        if (!resourceMenus.isEmpty()) {
            appRes.put("menu", menus);
        }
        if (!resourceButtons.isEmpty()) {
            appRes.put("button", buttons);
        }
        ResourceDTO resource = new ResourceDTO();
        resource.setApplicationName(app.getName());
        resource.setApplicationCode(app.getCode());
        resource.setAppRes(appRes);
        List<ResourceDTO> resourceList = new ArrayList<>();
        resourceList.add(resource);
        return resourceList;
    }

    //获取多个租户下权限所对应的的资源信息, 并按应用拆分
    //入参: 多个租户下的角色权限信息, key为租户id  value为租户下的角色权限信息
    //返回值: {租户A:[{应用1资源信息},{应用2资源信息}],租户B:[{},{}...]}
    @Override
    public Map<Long, List<ResourceDTO>> loadResources(Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap) {
        /*入参:
        {1151761810982985002=AuthorizationInfoDTO(rolePrivilegeMap={r_002=[p_002, p_003]}),
         1151761810982985003=AuthorizationInfoDTO(rolePrivilegeMap={r_005=[p_005, p_004, p_007, p_006]})}*/
        if (tenantAuthorizationInfoMap.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_110006);
        }

        Map<Long, List<ResourceDTO>> resultMap = new HashMap<>();
        //遍历某用户在多个租户下的权限信息
        for (Map.Entry<Long, AuthorizationInfoDTO> entry : tenantAuthorizationInfoMap.entrySet()) {
            Long tenantId = entry.getKey();//租户id
            AuthorizationInfoDTO authInfo = entry.getValue();//租户下的角色权限map  结构: rolecode1 [pcode1,pcode2]
            Map<String, List<String>> rolePrivilegeMap = authInfo.getRolePrivilegeMap();
            //遍历角色权限的map
            Set<String> privilegeCodeSet = new HashSet<>();//权限编码集合
            for (Map.Entry<String, List<String>> entry2 : rolePrivilegeMap.entrySet()) {//key为角色编码  value为权限编码
                //roleCode = entry2.getKey();
                //租户某个角色下的权限编码list, 一个角色可能对应多个权限
                List<String> pcodelist = entry2.getValue();
                //放入set集合
                privilegeCodeSet.addAll(pcodelist);
            }
            //根据权限编码获取对应的资源(菜单和按钮)
            //应用 -资源    权限编码可以查到资源(菜单, 按钮)和应用
            List<ResourceMenu> resourceMenus = menuMapper.selectList(new QueryWrapper<ResourceMenu>().lambda()
                    .in(ResourceMenu::getPrivilegeCode, privilegeCodeSet));
            List<ResourceButton> resourceButtons = buttonMapper.selectList(new QueryWrapper<ResourceButton>().lambda()
                    .in(ResourceButton::getPrivilegeCode, privilegeCodeSet));

            //按照应用划分 组装数据
            //key应用 value是应用对应的资源
            List<ResourceDTO> resourceDTOS = new ArrayList<>();

            Map<String, List<ResourceMenu>> tempMenuMap = new HashMap<>();
            for (ResourceMenu m : resourceMenus) {
                String appCode = m.getApplicationCode();
                if (tempMenuMap.containsKey(appCode)) {
                    tempMenuMap.get(appCode).add(m);
                } else {
                    List<ResourceMenu> menuList = new ArrayList<>();
                    menuList.add(m);
                    tempMenuMap.put(appCode, menuList);
                }
            }
            Map<String, List<ResourceButton>> tempButtonMap = new HashMap<>();
            for (ResourceButton m : resourceButtons) {
                String appCode = m.getApplicationCode();
                if (tempButtonMap.containsKey(appCode)) {
                    tempButtonMap.get(appCode).add(m);
                } else {
                    List<ResourceButton> menuList = new ArrayList<>();
                    menuList.add(m);
                    tempButtonMap.put(appCode, menuList);
                }
            }
            for (Map.Entry<String, List<ResourceMenu>> tempEntry : tempMenuMap.entrySet()) {
                ResourceDTO resourceDTO = new ResourceDTO();
                resourceDTO.setApplicationCode(tempEntry.getKey());
                Map map1 = new HashMap();
                map1.put("menu", tempEntry.getValue());
                if (tempButtonMap.get(tempEntry.getKey()) != null) {
                    map1.put("button", tempButtonMap.get(tempEntry.getKey()));
                }
                resourceDTO.setAppRes(map1);
                resourceDTOS.add(resourceDTO);
            }
            resultMap.put(tenantId, resourceDTOS);
        }
        return resultMap;
    }

    // /**
    //  * 获取多个租户下权限所对应的的资源信息, 并按应用拆分
    //  *
    //  * @param tenantAuthorizationInfoMap 多个租户下的角色权限信息, key为租户id  value为租户下的角色权限信息
    //  * @return 多个租户下的资源信息, key为租户id  value为租户下多个应用的资源信息
    //  * 如: {租户A:[{应用1资源信息},{应用2资源信息}],租户B:[{},{}...]}
    //  */
    // @Override
    // public Map<Long, List<ResourceDTO>> loadResources(Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap) {
    //     if (tenantAuthorizationInfoMap.isEmpty()) {
    //         throw new BusinessException(CommonErrorCode.E_200202);
    //     }
    //     /**
    //      * 1.传入多个 租户(key)-角色权限  最终要租户-按应用划分-资源信息
    //      * 2.根据权限获取资源loadResources(List<String> privilegeCodes, String applicationCode)
    //      */
    //
    //     Map<Long, List<ResourceDTO>> resultResource = new HashMap<>();
    //     //Map<String, List<ResourceDTO>> tmpMap = new HashMap<>();
    //     //遍历map 获取多个租户的角色权限信息
    //     for (Map.Entry<Long, AuthorizationInfoDTO> entry : tenantAuthorizationInfoMap.entrySet()) {
    //         AuthorizationInfoDTO dto = entry.getValue();//结构: rolecode1 [pcode1,pcode2]
    //         Map<String, List<String>> rolePrivilegeMap = dto.getRolePrivilegeMap();//角色-关系数据Map<String,List<String>>
    //
    //         Set<String> privilegeCodeSet = new HashSet<>();//权限编码集合
    //         //遍历角色权限的map Map<String,List<String>> 结构: rolecode1 [pcode1,pcode2]
    //         for (Map.Entry<String, List<String>> entry2 : rolePrivilegeMap.entrySet()) {
    //
    //             List<String> pcodelist = entry2.getValue();//租户某个角色下的权限list, 一个角色可能对应多个权限
    //             //根据权限获取资源
    //             privilegeCodeSet.addAll(pcodelist);
    //         }
    //
    //         List<String> privilegeList = new ArrayList<>(privilegeCodeSet);
    //         //查询到权限对应的资源和应用
    //         List<ResourceAPPDTO> result = menuMapper.selectResource(privilegeList);
    //         List<ResourceDTO> r = new ArrayList<>();
    //         for (ResourceAPPDTO resApp : result) {
    //             //应用和资源(包含所属应用编码)
    //             *//*ResourceDTO resources  = new ResourceDTO();
    //             Application entity = applicationDao.selectByCode(applicationCode);
    //             logger.info("[查找到的应用]{}", entity);
    //             if (entity == null || StringUtils.isBlank(entity.getCode())) {
    //                 logger.info("[查询应用为空]");
    //                 throw new BusinessException(ErrorCode.E_110106);
    //             }
    //             resources.setApplicationCode(entity.getCode());
    //             resources.setApplicationName(entity.getName());
    //             for (ResourceLoader resourceLoader : getResourceLoaders()) {
    //                 String resource = resourceLoader.load(privileageCodes, applicationCode);
    //                 logger.info("[loader]>>{}", resource);
    //                 resources.getAppRes().put(resourceLoader.getType(), JSONObject.parseObject(resource));
    //             }
    //             logger.info("[资源加载器, 返回]{}", resources);*//*
    //
    //
    //             //if(tmpMap.containsKey(entry.getKey() + "" + resApp.getApplicationCode())) {
    //             ResourceDTO resourceDTO = new ResourceDTO();
    //             resourceDTO.setApplicationCode(resApp.getApplicationCode());
    //             resourceDTO.setApplicationName(resApp.getName());
    //
    //             ResourceMenu m = new ResourceMenu();
    //             BeanUtils.copyProperties(resApp, m);
    //             //m.setParentId(resApp.getParentId());
    //             //m.setTitle(resApp.getTitle());
    //             //m.setUrl(resApp.getUrl());
    //             //m.setIcon(resApp.getIcon());
    //             //m.setSort(resApp.getSort());
    //             //m.setComment(resApp.getComment());
    //             //m.setStatus(resApp.getStatus());
    //             //m.setApplicationCode(resApp.getApplicationCode());
    //             //m.setPrivilegeCode(resApp.getPrivilegeCode());
    //
    //             List<ResourceMenu> obj = new ArrayList<>();
    //             obj.add(m);
    //             resourceDTO.getAppRes().put("menu", obj);//不同类型的资源加载 待完善 注入一个接口, 获取资源类型的list
    //             r.add(resourceDTO);
    //             // }
    //         }
    //         resultResource.put(entry.getKey(), r);
    //     }
    //     return resultResource;
    // }

}
