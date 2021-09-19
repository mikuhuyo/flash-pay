package com.flash.operation.controller;

import com.flash.common.domain.PageVO;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.dto.MerchantQueryDto;
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
@RestController
@Api(value = "运营平台应用接口", tags = {"Operation"})
public class OperationController {
    @Reference
    private IMerchantService merchantService;

    @PostMapping("/m/merchants/audit/{auditStatus}")
    @ApiOperation("商户资质申请审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "某个商户id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态 2-审核通过,3-审核拒绝", required = true, dataType = "int", paramType = "path")
    })
    public void auditMerchant(@RequestParam Long merchantId, @PathVariable String auditStatus) {
        // 根据传入的商户标识, 进行审核
        merchantService.verifyMerchant(merchantId, auditStatus);
    }

    @GetMapping("/m/merchants")
    @ApiOperation("根据id商户获取商户信息")
    @ApiImplicitParam(name = "id", value = "商户id", required = true, dataType = "Long", paramType = "query")
    public MerchantDto getMerchant(@RequestParam Long id) {
        return merchantService.queryMerchantById(id);
    }

    @PostMapping("/m/merchants/page")
    @ApiOperation("商户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantQuery", value = "商户查询条件", dataType = "MerchantQueryDTO", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<MerchantDto> queryMerchant(@RequestBody MerchantQueryDto merchantQuery, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return merchantService.queryMerchantPage(merchantQuery, pageNo, pageSize);
    }
}
