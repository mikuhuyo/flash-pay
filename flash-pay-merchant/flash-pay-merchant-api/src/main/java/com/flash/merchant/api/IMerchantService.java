package com.flash.merchant.api;

import com.flash.common.domain.BusinessException;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.dto.StaffDto;
import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.api.vo.MerchantDetailVo;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IMerchantService {
    /**
     * 为门店设置管理员
     *
     * @param storeId 门面ID
     * @param staffId 商户ID
     * @throws BusinessException 自定义异常
     */
    void bindStaffToStore(Long storeId, Long staffId) throws BusinessException;

    /**
     * 新增员工
     *
     * @param staffDto 员工信息
     * @return StaffDto
     * @throws BusinessException 自定义异常
     */
    StaffDto createStaff(StaffDto staffDto) throws BusinessException;

    /**
     * 新增门店
     *
     * @param storeDto 门店信息
     * @return StoreConvert
     * @throws BusinessException 自定义异常
     */
    StoreDto createStore(StoreDto storeDto) throws BusinessException;

    /**
     * 商户资质申请
     *
     * @param merchantId       商户id
     * @param merchantDetailVO 商户信息
     * @throws BusinessException 自定义异常
     */
    void applyMerchant(Long merchantId, MerchantDetailVo merchantDetailVO) throws BusinessException;

    /**
     * 根据租户id查询商户
     *
     * @param tenantId 租户id
     * @return MerchantDto
     */
    MerchantDto queryMerchantByTenantId(Long tenantId);

    /**
     * 新增商户
     *
     * @param merchantDto 商户信息
     * @return MerchantDto
     */
    MerchantDto createMerchant(MerchantDto merchantDto);

    /**
     * 根据id查询商户详细信息
     *
     * @param merchantId 商户id
     * @return MerchantDto
     */
    MerchantDto queryMerchantById(Long merchantId);
}
