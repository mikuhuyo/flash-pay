package com.flash.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.common.util.PhoneUtil;
import com.flash.common.util.StringUtil;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.dto.MerchantQueryDto;
import com.flash.merchant.api.dto.StaffDto;
import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.api.vo.MerchantDetailVo;
import com.flash.merchant.convert.MerchantConvert;
import com.flash.merchant.convert.StaffConvert;
import com.flash.merchant.convert.StoreConvert;
import com.flash.merchant.entity.Merchant;
import com.flash.merchant.entity.Staff;
import com.flash.merchant.entity.Store;
import com.flash.merchant.entity.StoreStaff;
import com.flash.merchant.mapper.MerchantMapper;
import com.flash.merchant.mapper.StaffMapper;
import com.flash.merchant.mapper.StoreMapper;
import com.flash.merchant.mapper.StoreStaffMapper;
import com.flash.user.api.TenantService;
import com.flash.user.api.dto.tenant.CreateTenantRequestDTO;
import com.flash.user.api.dto.tenant.TenantDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Service
public class IMerchantServiceImpl implements IMerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private StoreStaffMapper storeStaffMapper;

    @Reference
    private TenantService tenantService;

    @Override
    public void verifyMerchant(Long merchantId, String auditStatus) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        merchant.setAuditStatus(auditStatus);
        merchantMapper.updateById(merchant);
    }

    @Override
    public PageVO<MerchantDto> queryMerchantPage(MerchantQueryDto merchantQueryDto, Integer pageNo, Integer pageSize) throws BusinessException {
        IPage<Merchant> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        LambdaQueryWrapper<Merchant> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (merchantQueryDto != null) {
            lambdaQueryWrapper
                    .like(StringUtils.isNotBlank(merchantQueryDto.getMerchantName()), Merchant::getMerchantName, merchantQueryDto.getMerchantName())
                    .eq(StringUtils.isNotBlank(merchantQueryDto.getMobile()), Merchant::getMobile, merchantQueryDto.getMobile())
                    .eq(StringUtils.isNotBlank(merchantQueryDto.getMerchantType()), Merchant::getMerchantType, merchantQueryDto.getMerchantType())
                    .eq(merchantQueryDto.getAuditStatus() != null, Merchant::getAuditStatus, merchantQueryDto.getAuditStatus());
        }

        IPage<Merchant> result = merchantMapper.selectPage(page, lambdaQueryWrapper);
        if (result.getTotal() > 0) {
            List<MerchantDto> merList = MerchantConvert.INSTANCE.entityList2dtoList(result.getRecords());
            return new PageVO<MerchantDto>(merList, result.getTotal(), Long.valueOf(result.getCurrent()).intValue(), Long.valueOf(result.getSize()).intValue());
        }

        return new PageVO<MerchantDto>(new ArrayList<MerchantDto>(), 0, Long.valueOf(result.getCurrent()).intValue(), Long.valueOf(result.getSize()).intValue());
    }

    @Override
    public Boolean queryStoreInMerchantId(Long storeId, Long merchantId) throws BusinessException {
        return storeMapper.selectCount(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getMerchantId, merchantId)) > 0;
    }

    @Override
    public PageVO<StoreDto> queryStoreByPage(StoreDto storeDto, Integer page, Integer size) throws BusinessException {
        LambdaQueryWrapper<Store> storeLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 查询条件
        if (storeDto != null && storeDto.getMerchantId() != null) {
            storeLambdaQueryWrapper.eq(Store::getMerchantId, storeDto.getMerchantId());
        }
        if (storeDto != null && storeDto.getId() != null) {
            storeLambdaQueryWrapper.eq(Store::getId, storeDto.getId());
        }
        if (storeDto != null && storeDto.getStoreName() != null) {
            storeLambdaQueryWrapper.like(Store::getStoreName, storeDto.getStoreName());
        }

        IPage<Store> storeIPage = storeMapper.selectPage(new Page<Store>(page, size), storeLambdaQueryWrapper);
        List<StoreDto> storeDtos = StoreConvert.INSTANCE.entityList2dtoList(storeIPage.getRecords());
        return new PageVO<>(storeDtos, storeIPage.getTotal(), page, size);
    }

    @Override
    public void bindStaffToStore(Long storeId, Long staffId) throws BusinessException {
        StoreStaff storeStaff = new StoreStaff();
        storeStaff.setStoreId(storeId);
        storeStaff.setStaffId(staffId);
        storeStaffMapper.insert(storeStaff);
    }

    @Override
    public StaffDto createStaff(StaffDto staffDto) throws BusinessException {
        // 验证手机号
        String mobile = staffDto.getMobile();
        if (StringUtil.isBlank(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        if (!PhoneUtil.isMatches(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }

        // 判断用户名是否为空
        if (StringUtil.isBlank(staffDto.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }

        // 根据手机号判断员工是否已在指定商户存在
        if (!isExistStaffByMobile(mobile, staffDto.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }

        // 根据账号判断员工是否已在指定商户存在
        if (!isExistStaffByUsername(staffDto.getUsername(), staffDto.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }

        Staff staff = StaffConvert.INSTANCE.dto2entity(staffDto);
        staff.setLastLoginTime(new Date());
        staffMapper.insert(staff);
        return StaffConvert.INSTANCE.entity2dto(staff);
    }

    private Boolean isExistStaffByMobile(String mobile, Long merchantId) {
        LambdaQueryWrapper<Staff> eq = new LambdaQueryWrapper<Staff>().eq(Staff::getMobile, mobile).eq(Staff::getMerchantId, merchantId);
        return staffMapper.selectCount(eq) == 0;
    }

    private Boolean isExistStaffByUsername(String username, Long merchantId) {
        LambdaQueryWrapper<Staff> eq = new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, username).eq(Staff::getMerchantId, merchantId);
        return staffMapper.selectCount(eq) == 0;
    }

    @Override
    public StoreDto createStore(StoreDto storeDto) throws BusinessException {
        Store store = StoreConvert.INSTANCE.dto2entity(storeDto);
        storeMapper.insert(store);
        return StoreConvert.INSTANCE.entity2dto(store);
    }

    @Override
    public void applyMerchant(Long merchantId, MerchantDetailVo merchantDetailVO) throws BusinessException {
        MerchantDto merchantDto = MerchantConvert.INSTANCE.vo2dto(merchantDetailVO);

        Integer count = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getId, merchantId));
        if (count == null || count == 0) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }

        Merchant merchant = MerchantConvert.INSTANCE.dto2entity(merchantDto);

        merchant.setId(merchantId);
        // 1 已申请待审核
        merchant.setAuditStatus("1");
        merchant.setTenantId(merchantDto.getTenantId());

        merchantMapper.updateById(merchant);
    }

    @Override
    public MerchantDto queryMerchantByTenantId(Long tenantId) {
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>().eq(Merchant::getTenantId, tenantId));

        if (merchant == null) {
            return null;
        }

        return MerchantConvert.INSTANCE.entity2dto(merchant);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public MerchantDto createMerchant(MerchantDto merchantDto) {
        // 检验手机号是否为空
        if (StringUtil.isBlank(merchantDto.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        // 校验手机号的合法性
        if (!PhoneUtil.isMatches(merchantDto.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        // 联系人非空校验
        if (StringUtil.isBlank(merchantDto.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        // 密码非空校验
        if (StringUtil.isBlank(merchantDto.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }

        // 校验是否重复注册
        Integer count = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getMobile, merchantDto.getMobile()));
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }

        // 添加租户
        CreateTenantRequestDTO createTenantRequestDTO = new CreateTenantRequestDTO();
        createTenantRequestDTO.setMobile(merchantDto.getMobile());
        // 标注类型商户
        createTenantRequestDTO.setTenantTypeCode("shanju-merchant");
        // 初始化套餐
        createTenantRequestDTO.setBundleCode("shanju-merchant");
        createTenantRequestDTO.setUsername(merchantDto.getUsername());
        createTenantRequestDTO.setPassword(merchantDto.getPassword());
        // 新增租户为管理员
        createTenantRequestDTO.setName(merchantDto.getUsername());
        TenantDTO tenantAndAccount = tenantService.createTenantAndAccount(createTenantRequestDTO);
        if (tenantAndAccount == null) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }

        // 判断租户是否注册过商户
        Merchant data = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>().eq(Merchant::getTenantId, tenantAndAccount.getId()));
        if (data != null && data.getId() != null) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }

        // 为商户设置租户id
        merchantDto.setTenantId(tenantAndAccount.getId());
        // 标记状态-未审核
        // 0‐未申请,1‐已申请待审核,2‐审核通过,3‐审核拒绝
        merchantDto.setAuditStatus("0");
        // MybatisPlus在插入的时候会默认赋值id
        Merchant merchant = MerchantConvert.INSTANCE.dto2entity(merchantDto);
        merchantMapper.insert(merchant);

        // 新增根门店
        StoreDto storeDto = new StoreDto();
        storeDto.setMerchantId(merchant.getId());
        storeDto.setStoreName("根门店");
        StoreDto store = createStore(storeDto);

        // 新增员工
        StaffDto staffDto = new StaffDto();
        staffDto.setMerchantId(merchant.getId());
        staffDto.setMobile(merchant.getMobile());
        staffDto.setUsername(merchant.getUsername());
        staffDto.setStoreId(store.getId());
        StaffDto staff = createStaff(staffDto);

        // 为门店设置管理员
        bindStaffToStore(store.getId(), staff.getId());

        return MerchantConvert.INSTANCE.entity2dto(merchant);
    }

    @Override
    public MerchantDto queryMerchantById(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);

        if (merchant == null) {
            return null;
        }

        return MerchantConvert.INSTANCE.entity2dto(merchant);
    }
}
