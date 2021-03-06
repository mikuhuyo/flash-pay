package com.flash.merchant.api;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.PageVO;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.dto.MerchantQueryDto;
import com.flash.merchant.api.dto.StaffDto;
import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.api.vo.MerchantDetailVo;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IMerchantService {
    /**
     * 删除员工
     *
     * @param id 员工id
     * @throws BusinessException 自定义异常
     */
    void removeStaff(Long id) throws BusinessException;

    /**
     * 修改员工信息
     *
     * @param staff     员工信息
     * @param roleCodes 角色代码
     * @throws BusinessException 自定义异常
     */
    void modifyStaff(StaffDto staff, String[] roleCodes) throws BusinessException;

    /**
     * 查询员工详情
     *
     * @param id       员工id
     * @param tenantId 租户id
     * @return StaffDto
     */
    StaffDto queryStaffDetail(Long id, Long tenantId);

    /**
     * 商户新增员工和账号
     *
     * @param staffDto  员工信息
     * @param roleCodes 角色代码
     * @throws BusinessException
     */
    void createStaffAndAccount(StaffDto staffDto, String[] roleCodes) throws BusinessException;

    /**
     * 分页查询商户下的员工
     *
     * @param staffDto 员工信息
     * @param pageNo   页码
     * @param pageSize 数据条数
     * @return PageVO<StaffDto>
     * @throws BusinessException 自定义异常
     */
    PageVO<StaffDto> queryStaffByPage(StaffDto staffDto, Integer pageNo, Integer pageSize) throws BusinessException;

    /**
     * 删除某门店
     *
     * @param id 门店id
     * @throws BusinessException 自定义异常
     */
    void removeStore(Long id) throws BusinessException;

    /**
     * 修改门店
     *
     * @param store 门店信息
     * @throws BusinessException 自定义异常
     */
    void modifyStore(StoreDto store, List<Long> staffIds) throws BusinessException;

    /**
     * 查询某个门店
     *
     * @param id 门店id
     * @return StoreDto
     * @throws BusinessException 自定义异常
     */
    StoreDto queryStoreById(Long id) throws BusinessException;

    /**
     * 商户下新增门店, 并设置管理员
     *
     * @param storeDTO 门店信息
     * @throws BusinessException 自定义异常
     */
    StoreDto createStore(StoreDto storeDTO, List<Long> staffIds) throws BusinessException;

    /**
     * 商户资质审核
     *
     * @param merchantId  商户id
     * @param auditStatus 状态
     * @throws BusinessException 自定义异常
     */
    void verifyMerchant(Long merchantId, String auditStatus) throws BusinessException;

    /**
     * 商户分页条件查询
     *
     * @param merchantQueryDto 查询条件
     * @param pageNo           页码
     * @param pageSize         数据条数
     * @return PageVO<MerchantDto>
     * @throws BusinessException 自定义异常
     */
    PageVO<MerchantDto> queryMerchantPage(MerchantQueryDto merchantQueryDto, Integer pageNo, Integer pageSize) throws BusinessException;

    /**
     * 查询门店是否在指定商户下
     *
     * @param storeId    门店id
     * @param merchantId 商户id
     * @return true-属于, false-不属于
     * @throws BusinessException 自定义异常
     */
    Boolean queryStoreInMerchantId(Long storeId, Long merchantId) throws BusinessException;

    /**
     * 分页获取门店信息
     *
     * @param storeDto 门店查询条件
     * @param page     页码
     * @param size     每页的数据条数
     * @return PageVO<StoreDto>
     * @throws BusinessException 自定义异常
     */
    PageVO<StoreDto> queryStoreByPage(StoreDto storeDto, Integer page, Integer size) throws BusinessException;

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
