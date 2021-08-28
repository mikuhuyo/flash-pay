package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("authorization_role_privilege")
public class AuthorizationRolePrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField("ROLE_ID")
    private Long roleId;

    /**
     * 权限id
     */
    @TableField("PRIVILEGE_ID")
    private Long privilegeId;


}
