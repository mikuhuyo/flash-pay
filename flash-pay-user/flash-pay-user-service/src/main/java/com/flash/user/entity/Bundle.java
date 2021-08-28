package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bundle")
public class Bundle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 套餐名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 套餐编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 租户类型编码
     */
    @TableField("TENANT_TYPE_CODE")
    private String tenantTypeCode;

    /**
     * 套餐包含功能描述,JSON格式的角色与权限
     */
    @TableField("ABILITY")
    private String ability;

    /**
     * API调用次数/月
     */
    @TableField("NUMBER_OF_INVOCATION")
    private Integer numberOfInvocation;

    /**
     * 并发数/秒
     */
    @TableField("NUMBER_OF_CONCURRENT")
    private Integer numberOfConcurrent;

    /**
     * 允许创建应用数量
     */
    @TableField("NUMBER_OF_APP")
    private Integer numberOfApp;

    /**
     * 套餐说明
     */
    @TableField("COMMENT")
    private String comment;

    /**
     * 是否为初始化套餐
     */
    @TableField("INITIALIZE")
    private Boolean initialize;


}
