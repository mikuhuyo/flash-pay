package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 租户名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 租户类型编码
     */
    @TableField("TENANT_TYPE_CODE")
    private String tenantTypeCode;

    /**
     * 购买套餐编码
     */
    @TableField("BUNDLE_CODE")
    private String bundleCode;


}
