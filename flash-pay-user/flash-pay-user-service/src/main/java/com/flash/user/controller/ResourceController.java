package com.flash.user.controller;

import com.flash.common.domain.PageVO;
import com.flash.user.api.MenuService;
import com.flash.user.api.ResourceService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.menu.MenuDTO;
import com.flash.user.api.dto.menu.MenuQueryDTO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.resource.ApplicationQueryParams;
import com.flash.user.api.dto.resource.ResourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "统一账号-资源", tags = "统一账号-资源相关", description = "统一账号-资源相关")
@RestController
@Slf4j
public class ResourceController {
    @Reference
    private ResourceService resourceService;

    @Reference
    private MenuService menuService;

    /**
     * 创建应用
     * 会关联创建uaa服务中的接入客户端, 其中code为clientid
     *
     * @param application
     */
    @ApiOperation("创建应用")
    @ApiImplicitParam(name = "application", value = "创建应用信息", required = true, dataType = "ApplicationDTO", paramType = "body")
    @PostMapping("/apps")
    public void createApplication(@RequestBody ApplicationDTO application) {
        resourceService.createApplication(application);
    }

    @ApiOperation("修改应用")
    @ApiImplicitParam(name = "application", value = "应用信息", required = true, dataType = "ApplicationDTO", paramType = "body")
    @PutMapping("/apps")
    public void modifyApplication(@RequestBody ApplicationDTO application) {
        resourceService.modifyApplication(application);
    }

    @ApiOperation("删除应用")
    @ApiImplicitParam(name = "application", value = "应用信息", required = true, dataType = "String", paramType = "path")
    @Delete("/apps/{applicationCode}")
    public void removeApplication(@PathVariable String applicationCode) {
        resourceService.removeApplication(applicationCode);
    }

    @ApiOperation("根据应用编码查找应用")
    @ApiImplicitParam(name = "applicationCode", value = "应用信息", required = true, dataType = "String", paramType = "path")
    @GetMapping("/apps/{applicationCode}")
    public ApplicationDTO queryApplication(@PathVariable String applicationCode) {
        return resourceService.queryApplication(applicationCode);
    }

    @ApiOperation("分页条件查找应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query", value = "租户检索参数", required = true, dataType = "ApplicationQueryParams", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/apps/page")
    public PageVO<ApplicationDTO> pageApplicationByConditions(@RequestBody ApplicationQueryParams query, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return resourceService.pageApplicationByConditions(query, pageNo, pageSize);
    }


    @ApiOperation("根据权限加载指定应用的资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "privileageCodes", value = "权限集合", required = true, allowMultiple = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "applicationCode", value = "应用的编码", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/apps/privileges/resources/{applicationCode}")
    public List<ResourceDTO> loadResources(@RequestParam List<String> privileageCodes, @PathVariable String applicationCode) {
        return resourceService.loadResources(privileageCodes, applicationCode);
    }

    @ApiOperation("获取多个租户下权限所对应的的资源信息, 并按应用拆分")
    @ApiImplicitParam(name = "tenantAuthorizationInfoMap", value = "多个租户下的资源信息", required = true, dataType = "Map<Long, AuthorizationInfoDTO>", paramType = "body")
    @GetMapping("/tenants/apps/privileges/resources")
    public Map<Long, List<ResourceDTO>> loadResources(@RequestBody Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap) {
        return resourceService.loadResources(tenantAuthorizationInfoMap);
    }

    @ApiOperation("根据ID查询菜单")
    @ApiImplicitParam(name = "id", value = "菜单Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/menus/{id}")
    public MenuDTO queryMenu(@PathVariable Long id) {
        return menuService.queryMenu(id);
    }

    @ApiOperation("根据应用编码查询菜单列表")
    @ApiImplicitParam(name = "applicationCode", value = "应用编码", required = true, dataType = "String", paramType = "path")
    @GetMapping("/menus/{applicationCode}/menu-list")
    public List<MenuDTO> queryMenuByApplicationCode(@PathVariable String applicationCode) {
        return menuService.queryMenuByApplicationCode(applicationCode);
    }

    @ApiOperation("根据条件查询菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "查询菜单参数", required = true, dataType = "MenuQueryDTO", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/menus/page")
    public PageVO<MenuDTO> queryMenu(@RequestBody MenuQueryDTO params, @RequestParam Integer pageNo, Integer pageSize) {
        return menuService.queryMenu(params, pageNo, pageSize);
    }

    @ApiOperation("根据权限编码列表获取菜单")
    @ApiImplicitParam(name = "privileges", value = "权限编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    @GetMapping("/menus/privileges")
    public List<MenuDTO> queryMenuByPrivileges(@RequestParam String[] privileges) {
        return menuService.queryMenuByPrivileges(privileges);
    }

}
