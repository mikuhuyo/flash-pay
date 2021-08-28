package com.flash.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("authorization_privilege")
public class AuthorizationPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 权限编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 所属权限组id
     */
    @TableField("PRIVILEGE_GROUP_ID")
    private Long privilegeGroupId;


}
