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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/my/stores/merchants/page")
    @ApiOperation("分页查询商户下的门店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSie", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<StoreDto> queryStoreByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Long merchantId = SecurityUtil.getMerchantId();
        StoreDto storeDto = new StoreDto();
        storeDto.setMerchantId(merchantId);

        return iMerchantService.queryStoreByPage(storeDto, pageNo, pageSize);
    }
}
