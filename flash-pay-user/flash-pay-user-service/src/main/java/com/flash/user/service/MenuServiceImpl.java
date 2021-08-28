package com.flash.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.PageVO;
import com.flash.user.api.MenuService;
import com.flash.user.api.dto.menu.MenuDTO;
import com.flash.user.api.dto.menu.MenuQueryDTO;
import com.flash.user.convert.ResourceMenuConvert;
import com.flash.user.entity.ResourceMenu;
import com.flash.user.mapper.ResourceMenuMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private ResourceMenuMapper resourceMenuMapper;


    /**
     * 根据ID查询菜单
     *
     * @param id
     * @return
     */
    @Override
    public MenuDTO queryMenu(Long id) {
        ResourceMenu resourceMenu = resourceMenuMapper.selectById(id);
        MenuDTO menuDTO = ResourceMenuConvert.INSTANCE.entity2dto(resourceMenu);
        return menuDTO;
    }

    /**
     * 根据应用编码查询菜单列表
     *
     * @param applicationCode
     * @return
     */
    @Override
    public List<MenuDTO> queryMenuByApplicationCode(String applicationCode) {
        List<ResourceMenu> resourceMenus = resourceMenuMapper.selectList(new QueryWrapper<ResourceMenu>().lambda()
                .eq(ResourceMenu::getApplicationCode, applicationCode));
        List<MenuDTO> menuDTOS = ResourceMenuConvert.INSTANCE.entitylist2dto(resourceMenus);
        return menuDTOS;
    }

    /**
     * 根据条件查询菜单列表
     *
     * @param params
     * @param pageSize
     * @param pageNo
     */
    @Override
    public PageVO<MenuDTO> queryMenu(MenuQueryDTO params, Integer pageNo, Integer pageSize) {
        //参数 applicationCode(app表) title status(菜单表)
        pageSize = (pageSize == null ? 20 : pageSize);
        Page<MenuDTO> page = new Page<>(pageNo, pageSize);// 当前页, 总条数 构造 page 对象
        List<MenuDTO> menus = resourceMenuMapper.selectByPage(page, params);
        return new PageVO<>(menus, page.getTotal(), pageNo, pageSize);
    }

    /**
     * 根据权限编码列表获取菜单
     *
     * @param privileges 权限列表
     * @return
     */
    @Override
    public List<MenuDTO> queryMenuByPrivileges(String[] privileges) {
        List<String> privilege = Arrays.asList(privileges);
        List<ResourceMenu> resourceMenus = resourceMenuMapper.selectList(new QueryWrapper<ResourceMenu>().lambda()
                .in(ResourceMenu::getPrivilegeCode, privilege));
        List<MenuDTO> menuDTOS = ResourceMenuConvert.INSTANCE.entitylist2dto(resourceMenus);
        return menuDTOS;
    }
}
