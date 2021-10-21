package com.flash.merchant.controller;

import com.flash.common.domain.PageVO;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.StaffDto;
import com.flash.merchant.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@Api(value = "商户平台-成员(员工)管理", tags = "Staff")
@RestController
public class StaffController {

    @Reference
    private IMerchantService merchantService;

    @DeleteMapping("/my/staffs/{id}")
    @ApiOperation("删除某员工")
    @ApiImplicitParam(name = "id", value = "员工id", required = true, dataType = "Long", paramType = "path")
    public void removeStaff(@PathVariable("id") Long id) {
        merchantService.removeStaff(id);
    }

    @PutMapping("/my/staffs")
    @ApiOperation("修改某员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staff", value = "员工信息,账号和用户名不可修改", required = true, dataType = "StaffDTO", paramType = "body"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", allowMultiple = true, dataType = "String", paramType = "query")
    })
    public void modifyStaff(@RequestBody StaffDto staff, @RequestParam("roleCodes") String[] roleCodes) {
        merchantService.modifyStaff(staff, roleCodes);
    }

    @GetMapping("/my/staffs/{id}")
    @ApiOperation("查询员工详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "员工id", dataType = "Long", paramType = "path")
    })
    public StaffDto queryStaff(@PathVariable("id") Long id) {
        Long tenantId = SecurityUtil.getUser().getTenantId();
        return merchantService.queryStaffDetail(id, tenantId);
    }

    @PostMapping("/my/staffs")
    @ApiOperation("在某商户下创建员工和账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staff", value = "员工信息", required = true, dataType = "StaffDTO", paramType = "body"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    public void createStaffAndAccount(@RequestBody StaffDto staff, @RequestParam("roleCodes") String[] roleCodes) {
        Long merchantId = SecurityUtil.getMerchantId();
        staff.setMerchantId(merchantId);
        merchantService.createStaffAndAccount(staff, roleCodes);
    }

    @PostMapping("/my/staffs/page")
    @ApiOperation("分页查询商户下员工")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "staffDTO", value = "员工参数", dataType = "StaffDTO", paramType = "body"),
        @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<StaffDto> queryStaffByPage(@RequestBody StaffDto staffDto, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Long merchantId = SecurityUtil.getMerchantId();
        staffDto.setMerchantId(merchantId);
        return merchantService.queryStaffByPage(staffDto, pageNo, pageSize);
    }
}
