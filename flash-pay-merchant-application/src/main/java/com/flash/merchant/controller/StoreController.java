package com.flash.merchant.controller;

import com.flash.common.domain.PageVO;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@RestController
@Api(value = "商户平台-门店管理", tags = {"Store"})
public class StoreController {

    @Reference
    private IMerchantService iMerchantService;

    @DeleteMapping("/my/stores/{id}")
    @ApiOperation("删除某门店")
    @ApiImplicitParam(name = "id", value = "门店id", required = true, dataType = "Long", paramType = "path")
    public void removeStore(@PathVariable("id") Long id) {
        iMerchantService.removeStore(id);
    }

    @PutMapping("/my/stores")
    @ApiOperation("修改某门店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store", value = "门店信息", required = true, dataType = "StoreDTO", paramType = "body"),
            @ApiImplicitParam(name = "staffIds", value = "管理员id集合", required = true, allowMultiple = true, dataType = "Long", paramType = "query")
    })
    public void modifyStaff(@RequestBody StoreDto store, @RequestParam("staffIds") List<Long> staffIds) {
        iMerchantService.modifyStore(store, staffIds);
    }

    @GetMapping("/my/stores/{id}")
    @ApiOperation("查询某个门店的信息")
    @ApiImplicitParam(name = "id", value = "门店id", dataType = "Long", paramType = "path")
    public StoreDto queryStaff(@PathVariable("id") Long id) {
        return iMerchantService.queryStoreById(id);
    }

    @PostMapping("/my/stores/merchants/page")
    @ApiOperation("门店列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeDTO", value = "门店dto", dataType = "StoreDTO", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<StoreDto> queryStoreByPage(@RequestBody StoreDto storeDto, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        // 商户id
        Long merchantId = SecurityUtil.getMerchantId();
        // 商户id
        storeDto.setMerchantId(merchantId);
        return iMerchantService.queryStoreByPage(storeDto, pageNo, pageSize);
    }

    @PostMapping(value = "/my/stores")
    @ApiOperation("在某商户下新增门店, 并设置管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store", value = "门店信息", required = true, dataType = "StoreDTO", paramType = "body"),
            @ApiImplicitParam(name = "staffIds", value = "管理员id集合", required = true, allowMultiple = true, dataType = "Long", paramType = "query")
    })
    public void createStore(@RequestBody StoreDto store, @RequestParam("staffIds") List<Long> staffIds) {
        store.setMerchantId(SecurityUtil.getMerchantId());
        store.setStoreStatus(true);
        iMerchantService.createStore(store, staffIds);
    }

    @PostMapping("/my/stores/merchants/page")
    @ApiOperation("分页查询商户下的门店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSie", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<StoreDto> queryStoreByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Long merchantId = SecurityUtil.getMerchantId();
        StoreDto storeDto = new StoreDto();
        storeDto.setMerchantId(merchantId);

        return iMerchantService.queryStoreByPage(storeDto, pageNo, pageSize);
    }
}
