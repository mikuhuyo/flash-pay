package com.flash.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.common.util.PhoneUtil;
import com.flash.common.util.StringUtil;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.*;
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
import com.flash.user.api.AuthorizationService;
import com.flash.user.api.TenantService;
import com.flash.user.api.dto.authorization.RoleDTO;
import com.flash.user.api.dto.tenant.CreateAccountRequestDTO;
import com.flash.user.api.dto.tenant.CreateTenantRequestDTO;
import com.flash.user.api.dto.tenant.TenantDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
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

    @Reference
    private AuthorizationService authService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void removeStaff(Long id) {
        Staff staff = staffMapper.selectById(id);
        // 清除员工和门店的关系
        storeStaffMapper.delete(new QueryWrapper<StoreStaff>().lambda().eq(StoreStaff::getStaffId, staff.getId()));

        // 删除员工对应的账号, 账号-角色之间的关系
        // 获取商户的租户ID
        Long tenantId = queryMerchantById(staff.getMerchantId()).getTenantId();
        // 将某账号从租户内移除, 租户管理员不可移除
        tenantService.unbindTenant(tenantId, staff.getUsername());

        // 删除员工
        staffMapper.deleteById(staff);
    }

    @Override
    public void modifyStaff(StaffDto staffDto, String[] roleCodes) {
        Staff staff = staffMapper.selectById(staffDto.getId());
        if (staff == null) {
            throw new BusinessException(CommonErrorCode.E_200013);
        }
        // 更新员工的信息
        staff.setFullName(staffDto.getFullName());
        staff.setPosition(staffDto.getPosition());
        staff.setStoreId(staffDto.getStoreId());
        staffMapper.updateById(staff);

        // 处理员工的角色是否有修改
        Long tenantId = queryMerchantById(staff.getMerchantId()).getTenantId();
        tenantService.getAccountRoleBind(staff.getUsername(), tenantId, roleCodes);
    }

    @Override
    public StaffDto queryStaffDetail(Long id, Long tenantId) {
        StaffDto staff = queryStaffById(id);

        // 根据用户名和租户ID查询角色信息
        List<RoleDTO> roles = authService.queryRolesByUsername(staff.getUsername(), tenantId);
        List<StaffRoleDto> staffRoles = new ArrayList<>();
        if (!roles.isEmpty()) {
            roles.forEach(roleDTO -> {
                StaffRoleDto staffRoleDto = new StaffRoleDto();
                BeanUtils.copyProperties(roleDTO, staffRoleDto);
                staffRoles.add(staffRoleDto);
            });
        }
        staff.setRoles(staffRoles);
        return staff;
    }

    /**
     * 获取员工详情
     *
     * @param id 员工id
     * @return StaffDto
     */
    public StaffDto queryStaffById(Long id) {
        Staff staff = staffMapper.selectById(id);
        if (staff == null) {
            throw new BusinessException(CommonErrorCode.E_200013);
        }
        StaffDto dto = StaffConvert.INSTANCE.entity2dto(staff);
        // 根据员工归属门店id, 获取门店信息
        StoreDto store = queryStoreById(staff.getStoreId());
        dto.setStoreName(store.getStoreName());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void createStaffAndAccount(StaffDto staff, String[] roleCodes) {
        // 新增员工
        createStaff(staff);

        // 获取商户的租户ID
        Long tenantId = queryMerchantById(staff.getMerchantId()).getTenantId();
        CreateAccountRequestDTO accountRequest = new CreateAccountRequestDTO();
        accountRequest.setMobile(staff.getMobile());
        accountRequest.setUsername(staff.getUsername());
        accountRequest.setPassword(staff.getPassword());

        // 在租户内创建账号并绑定角色, 包含校验账号是否存在及角色是否绑定
        tenantService.checkCreateStaffAccountRole(tenantId, accountRequest, roleCodes);
    }

    @Override
    public PageVO<StaffDto> queryStaffByPage(StaffDto StaffDto, Integer pageNo, Integer pageSize) {
        // 创建分页
        Page<Staff> page = new Page<>(pageNo, pageSize);
        // 构造查询条件
        QueryWrapper<Staff> qw = new QueryWrapper();
        if (StaffDto != null) {
            if (StaffDto.getMerchantId() != null) {
                qw.lambda().eq(Staff::getMerchantId, StaffDto.getMerchantId());
            }
            if (StringUtils.isNotBlank(StaffDto.getUsername())) {
                qw.lambda().like(Staff::getUsername, StaffDto.getUsername());
            }
            if (StringUtils.isNotBlank(StaffDto.getFullName())) {
                qw.lambda().like(Staff::getFullName, StaffDto.getFullName());
            }
            if (StaffDto.getStaffStatus() != null) {
                qw.lambda().eq(Staff::getStaffStatus, StaffDto.getStaffStatus());
            }
        }
        // 执行查询
        IPage<Staff> staffIPage = staffMapper.selectPage(page, qw);
        // entity List转DTO List
        List<StaffDto> staffList = StaffConvert.INSTANCE.entityList2dtoList(staffIPage.getRecords());
        // 封装结果集
        PageVO<StaffDto> StaffDtoS = new PageVO<>(staffList, staffIPage.getTotal(), pageNo, pageSize);

        if (StaffDtoS.getCounts() == 0) {
            return StaffDtoS;
        }

        //添加员工所属门店信息
        for (StaffDto staffs : StaffDtoS) {
            StoreDto StoreDto = queryStoreById(staffs.getStoreId());
            if (StoreDto != null) {
                staffs.setStoreName(StoreDto.getStoreName());
            }
        }
        return StaffDtoS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeStore(Long id) {
        Store store = storeMapper.selectById(id);
        // 根门店不可删除
        if (store.getParentId() == null) {
            throw new BusinessException(CommonErrorCode.E_200018);
        }
        // 清除门店和员工关系
        storeStaffMapper.delete(new QueryWrapper<StoreStaff>().lambda().eq(StoreStaff::getStoreId, id));
        // 清除员工所属的门店
        staffMapper.update(null, new LambdaUpdateWrapper<Staff>().eq(Staff::getStoreId, id).set(Staff::getStoreId, null));
        // 删除门店
        storeMapper.deleteById(id);
    }

    @Override
    public void modifyStore(StoreDto dto, List<Long> staffIds) {
        // 更新门店的信息
        Store store = StoreConvert.INSTANCE.dto2entity(dto);
        storeMapper.updateById(store);

        if (staffIds != null) {
            // 清除门店绑定的管理员
            storeStaffMapper.delete(new QueryWrapper<StoreStaff>().lambda().eq(StoreStaff::getStoreId, dto.getId()));
            // 重新绑定关系, 设置管理员
            staffIds.forEach(id -> {
                bindStaffToStore(store.getId(), id);
            });
        }
    }

    @Override
    public StoreDto queryStoreById(Long id) {
        Store store = storeMapper.selectById(id);
        StoreDto storeDTO = StoreConvert.INSTANCE.entity2dto(store);
        if (storeDTO != null) {
            List<StaffDto> staffs = queryStoreAdmin(id);
            storeDTO.setStaffs(staffs);
        }
        return storeDTO;
    }

    private List<StaffDto> queryStoreAdmin(Long storeId) {
        // 根据门店获取管理员的id
        List<StoreStaff> storeStaffs = storeStaffMapper.selectList(new LambdaQueryWrapper<StoreStaff>().eq(StoreStaff::getStoreId, storeId));
        List<Staff> staff = null;
        if (!storeStaffs.isEmpty()) {
            List<Long> staffIds = new ArrayList<>();
            // 查询结果不为空, 则遍历获取管理员id. 可能有多个
            storeStaffs.forEach(ss -> {
                Long staffId = ss.getStaffId();
                staffIds.add(staffId);
            });
            // 根据id获取管理员信息
            staff = staffMapper.selectBatchIds(staffIds);
        }
        return StaffConvert.INSTANCE.entityList2dtoList(staff);
    }

    @Override
    public StoreDto createStore(StoreDto storeDto, List<Long> staffIds) {
        // 设置根门店
        Long rootStoreId = getRootStore(storeDto.getMerchantId());
        storeDto.setParentId(rootStoreId);

        // 新增门店
        Store store = StoreConvert.INSTANCE.dto2entity(storeDto);
        storeMapper.insert(store);

        if (staffIds != null) {
            // 设置管理员
            staffIds.forEach(id -> {
                bindStaffToStore(store.getId(), id);
            });
        }
        return StoreConvert.INSTANCE.entity2dto(store);
    }

    /**
     * 查询根门店
     *
     * @param merchantId 商户id
     * @return Long
     */
    private Long getRootStore(Long merchantId) {
        Store store = storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getMerchantId, merchantId).isNull(Store::getParentId));
        if (store == null) {
            throw new BusinessException(CommonErrorCode.E_200014);
        }
        return store.getId();
    }


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
