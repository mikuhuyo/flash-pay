package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ChangeAccountPwdDTO", description = "重置账号密码")
public class ChangeAccountPwdDTO implements Serializable {

    @ApiModelProperty("账号Id")
    private Long accountId;

    @ApiModelProperty("账号名")
    private String userName;


    @ApiModelProperty("密码")
    private String password;

}
