package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.user.api.dto.menu.MenuDTO;
import com.flash.user.api.dto.menu.MenuQueryDTO;
import com.flash.user.api.dto.resource.ResourceAPPDTO;
import com.flash.user.entity.ResourceMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMenuMapper extends BaseMapper<ResourceMenu> {

    @Select("select count(*) from resource_menu m where m.APPLICATION_CODE=#{applicationCode}")
    int selectResourceByapplicationCode(@Param("applicationCode") String applicationCode);

    // @Select("<script>" +
    //         "select a.`NAME`,a.`CODE`,m.* from resource_menu m " +
    //         "LEFT JOIN resource_application a ON a.`CODE`=m.APPLICATION_CODE " +
    //         "where APPLICATION_CODE=#{applicationCode} " +
    //         "and PRIVILEGE_CODE in <foreach collection='privilegeCodes' item='item' open='(' separator=',' close=')'>#{item}</foreach> " +
    //         "</script>")
    // List<ResourceDTO> selectResourceByPrivilegeInApp(@Param("privilegeCodes") List<String> privilegeCodes, @Param("applicationCode") String applicationCode);

    @Select("<script>"
            + "select * from resource_menu m "
            + "LEFT JOIN resource_application a ON a.`CODE`=m.APPLICATION_CODE "
            + "<where>"
            + "<if test=\"params.applicationCode != null and params.applicationCode!=''\"> "
            + "and a.`CODE`= #{params.applicationCode} "
            + "</if>"
            + "<if test=\"params.title != null and params.title!=''\"> "
            + "and m.TITLE= #{params.title} "  //and m.TITLE like "%"#{params.title}"%"
            + "</if>"
            + "<if test=\"params.status != null and params.status!=''\"> "
            + "and m.STATUS= #{params.status} "
            + "</if>"
            + "</where>" +
            "</script>")
    List<MenuDTO> selectByPage(@Param("page") Page<MenuDTO> page, @Param("params") MenuQueryDTO params);

    @Select("<script>" +
            "select * from  resource_menu m " +
            "left join resource_application a on a.`CODE`= m.APPLICATION_CODE " +
            "where m.PRIVILEGE_CODE in <foreach collection='pcodes' item='item' open='(' separator=',' close=')'>#{item}</foreach> " +
            "</script>")
    List<ResourceAPPDTO> selectResource(@Param("pcodes") List<String> pcodes);

}
