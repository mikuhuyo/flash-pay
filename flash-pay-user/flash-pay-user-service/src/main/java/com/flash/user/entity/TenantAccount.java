package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tenant_account")
public class TenantAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 账号d
     */
    @TableField("ACCOUNT_ID")
    private Long accountId;

    /**
     * 是否是租户管理员
     */
    @TableField("IS_ADMIN")
    private Boolean isAdmin;


}
