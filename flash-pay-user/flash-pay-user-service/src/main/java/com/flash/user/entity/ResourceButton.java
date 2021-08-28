package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("resource_button")
public class ResourceButton implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 按钮标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 链接url
     */
    @TableField("URL")
    private String url;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 排序
     */
    @TableField("SORT")
    private Integer sort;

    /**
     * 说明
     */
    @TableField("COMMENT")
    private String comment;

    /**
     * 状态
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 所属应用编码
     */
    @TableField("APPLICATION_CODE")
    private String applicationCode;

    /**
     * 绑定权限
     */
    @TableField("PRIVILEGE_CODE")
    private String privilegeCode;


}
