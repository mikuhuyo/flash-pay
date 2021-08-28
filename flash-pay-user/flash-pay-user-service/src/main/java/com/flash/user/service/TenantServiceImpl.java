package com.flash.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.common.util.MD5Util;
import com.flash.common.util.PhoneUtil;
import com.flash.common.util.RandomStringUtil;
import com.flash.user.api.AuthorizationService;
import com.flash.user.api.ResourceService;
import com.flash.user.api.TenantService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.authorization.RoleDTO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.tenant.*;
import com.flash.user.convert.*;
import com.flash.user.entity.*;
import com.flash.user.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private BundleMapper bundleMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TenantAccountMapper tenantAccountMapper;
    @Autowired
    private AccountRoleMapper accountRoleMapper;
    @Autowired
    private AuthorizationRoleMapper roleMapper;
    @Autowired
    private AuthorizationRolePrivilegeMapper rolePrivilegeMapper;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceApplicationMapper resourceApplicationMapper;


    //////////////////////////////////////////租户相关的操作///////////////////////////////////////////////////
    @Autowired
    private RestTemplate restTemplate;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    /**
     * 创建租户
     * 新增租户, 新增租户管理员, 同时初始化套餐(预留: 将限流规则写入sentinel)
     * 1.若管理员用户名已存在, 禁止创建
     * 2.手机号已存在, 禁止创建
     * 3.创建根租户对应账号时, 需要手机号, 账号的用户名密码和盐需要随机生成
     *
     * @param createTenantRequest 创建租户请求信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantDTO createTenantAndAccount(CreateTenantRequestDTO createTenantRequest) {

        //1.判断手机号跟用户名在账号中是否存在
        String mobile = createTenantRequest.getMobile();
        if (isExistAccountByMobile(mobile)) {//为true表示 手机号已存在
            //throw new BusinessException(CommonErrorCode.E_200203);
            AccountDTO account = getAccountByMobile(mobile);
            Long accountId = account.getId();
            Boolean isAdmin = true;
            //获取到租户信息并返回
            TenantDTO tenant = getTenantByAccount(accountId, isAdmin);
            if (tenant == null) {
                //创建租户
                Tenant ten = createTenant(createTenantRequest);
                //绑定租户和账号的关系, 设置为管理员
                bindTenantAccount(ten.getId(), accountId, true);
                return TenantConvert.INSTANCE.entity2dto(ten);
            } else {
                return tenant;
            }

        } else {
            //2.新增租户
            Tenant tenant = createTenant(createTenantRequest);

            //3.创建管理员账号并绑定租户
            CreateAccountRequestDTO createAccountRequest = new CreateAccountRequestDTO();
            createAccountRequest.setMobile(mobile);
            createAccountRequest.setUsername(createTenantRequest.getUsername());
            createAccountRequest.setPassword(createTenantRequest.getPassword());
            createAccountInTenant(createAccountRequest, tenant.getId());

            //4.初始化套餐
            //设置租户套餐为初始化套餐
            if (tenant.getId() == null) {
                throw new BusinessException(CommonErrorCode.E_200012);
            }
            //设置初始化套餐, 更新权限
            log.info("初始化套餐");
            initBundle(tenant.getId(), createTenantRequest.getBundleCode());

            return TenantConvert.INSTANCE.entity2dto(tenant);
        }
    }

    private Tenant createTenant(CreateTenantRequestDTO createTenantRequest) {
        //新增租户
        Tenant tenant = new Tenant();
        //租户名随机生成
        tenant.setName(createTenantRequest.getName() + "_" + RandomStringUtil.getRandomString(6));
        //设置租户类型
        tenant.setTenantTypeCode(createTenantRequest.getTenantTypeCode());
        //设置套餐code
        tenant.setBundleCode(createTenantRequest.getBundleCode());
        tenantMapper.insert(tenant);
        return tenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantDTO createTenantRelateAccount(CreateTenantRequestDTO createTenantRequest) {
        AccountDTO accountDTO = this.getAccountByUsername(createTenantRequest.getUsername());
        if (accountDTO == null) {
            throw new BusinessException(CommonErrorCode.E_110001);
        }
        //2.新增租户
        Tenant tenant = new Tenant();
        tenant.setName(createTenantRequest.getName() + "_" + RandomStringUtil.getRandomString(8));
        //设置租户类型
        tenant.setTenantTypeCode(createTenantRequest.getTenantTypeCode());
        //设置套餐code
        tenant.setBundleCode(createTenantRequest.getBundleCode());
        int insert = tenantMapper.insert(tenant);

        bindTenantAccount(tenant.getId(), accountDTO.getId(), true);

        initBundle(tenant.getId(), createTenantRequest.getBundleCode());

        return TenantConvert.INSTANCE.entity2dto(tenant);
    }

    /**
     * 删除租户
     * 解绑租户下所有账号, 清除租户下账号-角色信息, 清除租户下角色, 删除租户
     *
     * @param id 租户id
     */
    @Override
    public void removeTenant(Long id) {
        //预留接口, 暂不实现
    }

    /**
     * 获取租户信息
     *
     * @param id
     * @return 租户信息
     */
    @Override
    public TenantDTO getTenant(Long id) {
        Tenant entity = tenantMapper.selectById(id);
        return TenantConvert.INSTANCE.entity2dto(entity);
    }

    /**
     * 检索租户
     *
     * @param tenantQuery 租户查询条件
     * @param pageNo      查询页
     * @param pageSize    页记录数
     * @param sortBy      排序字段
     * @param order       顺序
     * @return
     */
    @Override
    public PageVO<TenantDTO> queryTenants(TenantQueryDTO tenantQuery, Integer pageNo, Integer pageSize, String sortBy, String order) {

        Page<TenantDTO> page = new Page<>(pageNo, (pageSize == null || pageSize < 1 ? 10 : pageSize));// 当前页, 总条数 构造 page 对象
        List<TenantDTO> tenants = tenantMapper.selectTenantByPage(page, tenantQuery, sortBy, order);
        return new PageVO<>(tenants, page.getTotal(), pageNo, pageSize);
    }

    /**
     * 查询某租户类型的套餐列表(不包含初始化套餐)
     *
     * @param tenantType
     * @return
     */
    @Override
    public List<BundleDTO> queryBundleByTenantType(String tenantType) {
        QueryWrapper<Bundle> qw = new QueryWrapper<>();
        qw.lambda().eq(Bundle::getTenantTypeCode, tenantType).eq(Bundle::getInitialize, 0);
        List<Bundle> bundles = bundleMapper.selectList(qw);
        List<BundleDTO> dtoList = BundleConvert.INSTANCE.entitylist2dto(bundles);
        return dtoList;
    }

    /**
     * 获取某套餐信息
     *
     * @param bundleCode 套餐编码
     * @return 套餐信息
     */
    @Override
    public BundleDTO getBundle(String bundleCode) {
        QueryWrapper<Bundle> qw = new QueryWrapper<>();
        qw.lambda().eq(Bundle::getCode, bundleCode);
        Bundle entity = bundleMapper.selectOne(qw);
        //Bundle bundle = bundleMapper.selectBundleByCode(bundleCode);
        return BundleConvert.INSTANCE.entity2dto(entity);
    }

    /**
     * 查询所有套餐
     *
     * @return
     */
    @Override
    public List<BundleDTO> queryAllBundle() {
        List<Bundle> bundles = bundleMapper.selectList(null);
        return BundleConvert.INSTANCE.entitylist2dto(bundles);
    }

    /**
     * 切换租户套餐
     * 租户切换套餐操作会清除 原租户内的所有账号-角色关联数据  原套餐产生的角色权限数据,并将限流规则写入sentinel
     *
     * @param tenantId   租户id
     * @param bundleCode 套餐编码
     */
    @Override
    public void changeBundle(Long tenantId, String bundleCode) {
        //查询租户下所有的角色
        List<RoleDTO> roleDTOS = authorizationService.queryRole(tenantId);
        if (roleDTOS.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_110007);
        }
        List<Long> roleIds = new ArrayList<>();
        for (RoleDTO r : roleDTOS) {
            Long id = r.getId();
            roleIds.add(id);
        }
        //1.清除租户下所有账号和角色的关系
        accountRoleMapper.update(null, new UpdateWrapper<AccountRole>().lambda()
                .eq(AccountRole::getTenantId, tenantId).set(AccountRole::getRoleCode, null));

        //2.删除租户下的所有角色
        roleMapper.delete(new QueryWrapper<AuthorizationRole>().lambda()
                .eq(AuthorizationRole::getTenantId, tenantId));

        //3.删除租户下所有角色对应的权限
        rolePrivilegeMapper.delete(new QueryWrapper<AuthorizationRolePrivilege>().lambda()
                .in(AuthorizationRolePrivilege::getRoleId, roleIds));

        //4.初始化套餐信息
        initBundle(tenantId, bundleCode);

    }

    /**
     * 初始化租户套餐
     *
     * @param tenantId   租户id
     * @param bundleCode 套餐编码
     */
    @Override
    public void initBundle(Long tenantId, String bundleCode) {
        //设置套餐
        Bundle bundle = bundleMapper.selectOne(new QueryWrapper<Bundle>().lambda().eq(Bundle::getCode, bundleCode));
        if (bundle != null && StringUtils.isNotBlank(bundle.getAbility())) {
            String ability = bundle.getAbility();
            /* ability格式如下
             [
               {"name":"商户管理员","code": "r_001","privilegeCodes": ["p_m_001","p_m_002","p_m_003","p_m_004","p_m_005"]},
               {"name": "商户门店收银员","code": "r_002","privilegeCodes": ["p_m_014"]}
             ]*/
            //处理角色权限json(角色和权限的code), 为租户绑定角色和权限
            List<RoleDTO> roleDTOList = JSONObject.parseArray(ability, RoleDTO.class);
            List<AuthorizationRole> roles = AuthorizationRoleConvert.INSTANCE.dtolist2entity(roleDTOList);
            List<String> rCodes = new ArrayList<>();//构建该套餐中的角色集合
            roles.forEach(role -> rCodes.add(role.getCode()));

            //1.在指定租户下新增角色 操作authorization_role表
            roleMapper.createRoles(tenantId, roles);

            //2.为租户绑定角色 操作account_role表
            Account account = accountMapper.selectAccountInfoByTenantId(tenantId);
            if (account != null && StringUtils.isNotBlank(account.getUsername())) {
                //获取角色编码
                String[] rcodeArray = rCodes.toArray(new String[0]);
                //绑定租户-角色关系
                authorizationService.bindAccountRole(account.getUsername(), tenantId, rcodeArray);
            }

            //3.为租户下的角色绑定权限操作authorization_role_privilege表
            for (RoleDTO roleDTO : roleDTOList) {
                String code = roleDTO.getCode();
                List<String> pCodes = roleDTO.getPrivilegeCodes();
                String[] privilegeCodes = pCodes.toArray(new String[0]);
                authorizationService.roleBindPrivilege(tenantId, code, privilegeCodes);
            }
        }
    }

    /**
     * 新增套餐
     *
     * @param bundleDTO
     */
    @Override
    public void createBundle(BundleDTO bundleDTO) {
        Bundle bundle = BundleConvert.INSTANCE.dto2entity(bundleDTO);
        bundleMapper.insert(bundle);
    }

    /**
     * 更新套餐
     *
     * @param bundleDTO
     */
    @Override
    public void modifyBundle(BundleDTO bundleDTO) {
        Bundle bundle = BundleConvert.INSTANCE.dto2entity(bundleDTO);
        bundleMapper.updateById(bundle);
    }

    /**
     * 新创建账号并绑定至某租户
     * 1.若用户名已存在, 禁止创建
     * 2.手机号已存在, 禁止创建
     *
     * @param createAccountRequest 创建账号请求
     * @param tenantId             租户id
     */
    @Override
    public void createAccountInTenant(CreateAccountRequestDTO createAccountRequest, Long tenantId) {
        //1.创建账号
        AccountDTO accountDTO = createAccount(createAccountRequest);
        if (accountDTO.getId() == null) {
            throw new BusinessException(CommonErrorCode.E_110001);
        }
        log.info("将创建的账号绑定至某租户");
        //2.将账号绑定到租户
        bindTenantAccount(tenantId, accountDTO.getId(), true);
    }

    /**
     * 将已存在账号, 绑定到某租户
     * 界面上先通过手机号查询到账号信息, 再通过账号信息的username, 调用此接口, 加入租户
     *
     * @param tenantId 租户id
     * @param username 用户名
     */
    @Override
    public void bindTenant(Long tenantId, String username) {
        //1.根据用户名获取账号信息
        AccountDTO accountDTO = getAccountByUsername(username);
        //2.如果查询到账号, 则绑定关系
        if (accountDTO != null && accountDTO.getId() != null) {
            //插入关系表
            bindTenantAccount(tenantId, accountDTO.getId(), null);
        }
    }

    /**
     * 创建账号
     * 1.若用户名已存在, 禁止创建
     * 2.手机号已存在, 禁止创建
     *
     * @param createAccountRequest 创建账号请求
     */
    @Override
    public AccountDTO createAccount(CreateAccountRequestDTO createAccountRequest) {
        log.info("创建账号: " + JSON.toJSONString(createAccountRequest));

        //1.校验手机号格式及是否存在
        String mobile = createAccountRequest.getMobile();
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        if (isExistAccountByMobile(mobile)) {
            throw new BusinessException(CommonErrorCode.E_100113);//手机号在账号中已存在
        }
        //2.校验用户名是否为空及是否存在
        String username = createAccountRequest.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        if (isExistAccountByUsername(username)) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        //3.校验密码是否为空
        String password = createAccountRequest.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }

        //4.构造账号数据并保存
        Account account = new Account();
        String salt = RandomStringUtil.getRandomString(5);
        String md5pwd = MD5Util.getMd5(password + salt);
        account.setSalt(salt);
        account.setPassword(md5pwd);
        account.setUsername(username);
        account.setMobile(mobile);
        accountMapper.insert(account);

        return AccountConvert.INSTANCE.entity2dto(account);
    }

    /**
     * 修改账号密码
     *
     * @param accountRequest 创建账号请求
     */
    @Override
    public boolean accountPassword(ChangeAccountPwdDTO accountRequest) {
        if (accountRequest == null || accountRequest.getAccountId() == null || StringUtils.isBlank(accountRequest.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100101);
        }

        LambdaUpdateWrapper<Account> qw = new LambdaUpdateWrapper<>();
        qw.eq(Account::getId, accountRequest.getAccountId());
        qw.eq(Account::getUsername, accountRequest.getUserName());
        Account account = accountMapper.selectOne(qw);
        if (account == null) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        qw.set(Account::getPassword, MD5Util.getMd5(accountRequest.getPassword() + account.getSalt()));
        int result = accountMapper.update(null, qw);
        return result > 0;
    }

    /**
     * 绑定账号和租户的关系
     *
     * @param tenantId
     * @param accountId
     * @param isAdmin
     */
    public void bindTenantAccount(Long tenantId, Long accountId, Boolean isAdmin) {
        //设置为管理员, 需要在创建租户时传值过来
        isAdmin = (isAdmin == null ? false : isAdmin);
        log.info("绑定账号和租户的关系");
        tenantMapper.insertTenantAccount(tenantId, accountId, isAdmin);
    }

    /**
     * 将某账号从租户内移除, 租户管理员不可移除
     * 删员工的时调用这个接口
     *
     * @param tenantId 租户id
     * @param username 用户名
     */
    @Override
    public void unbindTenant(Long tenantId, String username) {
        //1.租户内非管理员账号
        List<TenantAccount> tenantAccounts = accountMapper.selectNotAdmin(tenantId, username);
        for (TenantAccount list : tenantAccounts) {

            //2.删除账号-租户的关系
            Long accountId = list.getAccountId();
            tenantAccountMapper.deleteAccountInTenant(tenantId, accountId);

            //3.账号和在本租户下的角色的关系
            accountRoleMapper.deleteByUsernameInTenant(tenantId, username);

            //4.判断员工账号是否被其他租户使用
            int i = tenantAccountMapper.selectTenantByUsernameInAccount(username);
            if (i > 0) {
                throw new BusinessException(CommonErrorCode.E_110008);
            }
            //5.如果没有被使用, 删除员工对应的账号
            QueryWrapper<Account> qw = new QueryWrapper<>();
            qw.lambda().eq(Account::getUsername, username);
            accountMapper.delete(qw);

        }
    }

    /**
     * 根据用户名判断账号是否存在
     *
     * @param username 用户名
     * @return
     */
    @Override
    public boolean isExistAccountByUsername(String username) {
        int i = accountMapper.selectAccountByName(username);
        return i > 0;
    }

    /**
     * 根据手机号判断账号是否存在
     *
     * @param mobile 手机号
     * @return
     */
    @Override
    public boolean isExistAccountByMobile(String mobile) {
        log.info("判断手机号在账号中是否存在");
        int i = accountMapper.selectAccountByMobile(mobile);
        return i > 0;
    }

    /**
     * 根据用户名获取账号信息
     *
     * @param username 用户名
     * @return 账号信息
     */
    @Override
    public AccountDTO getAccountByUsername(String username) {
        QueryWrapper<Account> qw = new QueryWrapper<>();
        qw.lambda().eq(Account::getUsername, username);
        Account account = accountMapper.selectOne(qw);
        return AccountConvert.INSTANCE.entity2dto(account);
    }

    /**
     * 根据手机号获取账号信息
     *
     * @param mobile 手机号
     * @return 账号信息
     */
    @Override
    public AccountDTO getAccountByMobile(String mobile) {
        QueryWrapper<Account> qw = new QueryWrapper<>();
        qw.lambda().eq(Account::getMobile, mobile);
        Account account = accountMapper.selectOne(qw);
        return AccountConvert.INSTANCE.entity2dto(account);
    }

    /**
     * 根据用户名判断账号是否在某租户内
     *
     * @param tenantId 租户id
     * @param username 用户名
     * @return
     */
    @Override
    public boolean isExistAccountInTenantByUsername(Long tenantId, String username) {
        int i = accountMapper.selectAccountInTenantByName(tenantId, username);
        return i > 0;
    }

    /**
     * 根据手机号判断账号是否在某租户内
     *
     * @param tenantId 租户id
     * @param mobile   手机号
     * @return
     */
    @Override
    public boolean isExistAccountInTenantByMobile(Long tenantId, String mobile) {
        int i = accountMapper.selectAccountInTenantByMobile(tenantId, mobile);
        return i > 0;
    }

    /**
     * 检索账号
     *
     * @param accountQuery 账号查询条件
     * @param pageNo       查询页
     * @param pageSize     页记录数
     * @param sortBy       排序字段
     * @param order        顺序
     * @return
     */
    @Override
    public PageVO<AccountDTO> queryAccount(AccountQueryDTO accountQuery, Integer pageNo, Integer pageSize, String sortBy, String order) {
        //参数 租户id username mobile
        Page<AccountDTO> page = new Page<>(pageNo, (pageSize == null || pageSize < 1 ? 10 : pageSize));// 当前页, 总条数 构造 page 对象
        List<AccountDTO> tenants = accountMapper.selectAccountByPage(page, accountQuery, sortBy, order);
        return new PageVO<>(tenants, page.getTotal(), pageNo, pageSize);
    }

    /**
     * 查询某账号所属租户列表
     *
     * @param username 用户名
     * @return
     */
    @Override
    public List<TenantDTO> queryAccountInTenant(String username) {
        AccountDTO accountDTO = getAccountByUsername(username);
        Long id = accountDTO.getId();
        List<TenantDTO> tenantDTOS = tenantMapper.selectAccountInTenant(id);
        return tenantDTOS;
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @Override
    public String sendMessage(String phone) {
        log.info("调用短信微服务发送验证码: 手机号:{}", phone);
        String url = smsUrl + "/generate?name=sms&effectiveTime=600";
        Map params = new HashMap();
        params.put("mobile", phone);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        //执行发送
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        log.info("验证码-返回值:{}", JSON.toJSONString(exchange));
        //接收返回结果
        Map map = (Map) exchange.getBody();
        Map resultMap = (Map) map.get("result");
        return resultMap.get("key").toString();
    }

    /**
     * 校验手机验证码
     *
     * @param smsKey
     * @param passwordOrMessage
     */
    private Map verificationMessageCode(String smsKey, String passwordOrMessage) {
        String url = smsUrl + "/verify?name=sms&verificationKey=" + smsKey + "&verificationCode=" + passwordOrMessage;
        log.info("调用短信微服务校验验证码: url:{}", url);
        //验证验证码是否正确
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(params);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        log.info("校验验证码: 返回值:{}", JSON.toJSONString(exchange));
        Map map = (Map) exchange.getBody();
        return map;
    }

    /**
     * 用户认证
     *
     * @param authenticationInfo 认证请求信息
     * @return 认证成功的账号信息
     */
    @Override
    public AccountDTO authentication(AuthenticationInfo authenticationInfo) {
        //判断用户名密码登录的   还是手机验证码登录的 类型: 前端传入
        //短信快捷认证, 用户名密码认证, 二维码认证等
        String type = authenticationInfo.getAuthenticationType();
        String userOrMobile = authenticationInfo.getPrincipal();
        String passwordOrMessage = authenticationInfo.getCertificate();

        //用户名密码认证
        if ("password".equals(type)) {
            //根据用户名判断是否存在
            AccountDTO account = getAccountByUsername(userOrMobile);
            if (account == null) {
                throw new BusinessException(CommonErrorCode.E_110001);
            }
            String salt = account.getSalt();
            String password1 = account.getPassword();
            String md5 = DigestUtils.md5Hex(passwordOrMessage + salt);
            //DigestUtils
            if (!md5.equals(password1)) {
                throw new BusinessException(CommonErrorCode.E_100115);
            }
            return account;

        } else if ("sms".equals(type)) {  //短信快捷认证
            //校验手机号格式
            if (!PhoneUtil.isMatches(userOrMobile)) {
                throw new BusinessException(CommonErrorCode.E_100109);
            }
            String smsKey = authenticationInfo.getSmsKey();
            //校验手机短信验证码
            Map map = verificationMessageCode(smsKey, passwordOrMessage);
            if (map.get("result") == null || !(Boolean) map.get("result")) {
                throw new BusinessException(CommonErrorCode.E_100102);
            }
            //根据手机号判断用户是否存在
            boolean byMobile = isExistAccountByMobile(userOrMobile);
            if (!byMobile) {
                throw new BusinessException(CommonErrorCode.E_110001);
            }
            AccountDTO account = getAccountByMobile(userOrMobile);
            return account;
        }
        return null;
    }

    /**
     * 用户登录, 被uaa服务调用并生成令牌
     * 1.调用authentication(AuthenticationInfo authenticationInfo)认证接口通过认证
     * 2.调用queryAccountInTenant(String username)获取该用户所属租户列表
     * 3.授权, 调用AuthorizationService.authorize(String username, Long[] tenantIds)获取该用户在多个租户下的权限
     * 4.获取资源,调用loadResources(Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfMap);, 获取该用户在多个租户下的资源
     *
     * @param loginRequest 登录请求
     * @return 登录响应, 包括用户的租户, 角色, 权限资源等信息
     * <p>
     * 备忘录:
     * 1.由于token设计的可以跨多个租户, 当前登入租户, 需由应用方(可默认选第一个租户)掌控并传递, 设计在有状态接口的参数中, 不然无法知道用户当前的登入租户.
     * 2.在uaa生成token前, 需要根据client_id获取当前接入应用所属租户ID(ResourceService.queryApplication(String applicationCode)),
     * 并放入token, 此tenantId作为限流的limitApp, 也是套餐拥有者标识, 用于套餐限制
     * 3.token不能跨不同租户开发的应用
     */
    @Override
    public LoginInfoDTO login(LoginRequestDTO loginRequest) {
        AuthenticationInfo auth = new AuthenticationInfo();
        BeanUtils.copyProperties(loginRequest, auth);
        //认证返回
        AccountDTO accountDTO = authentication(auth);
        String username = accountDTO.getUsername();

        LoginInfoDTO loginInfo = new LoginInfoDTO();
        //账号的id username mobile
        loginInfo.setMobile(accountDTO.getMobile());
        loginInfo.setUsername(accountDTO.getUsername());
        loginInfo.setId(accountDTO.getId());

        List<TenantDTO> tenantDTOS = queryAccountInTenant(username);
        if (!CollectionUtils.isEmpty(tenantDTOS)) {
            loginInfo.setTenants(tenantDTOS);
            Long[] tenantIds = new Long[tenantDTOS.size()];

            int i = 0;
            for (TenantDTO t : tenantDTOS) {
                tenantIds[i++] = t.getId();
            }
            //map-authorize key: 租户id value: 权限
            Map<Long, AuthorizationInfoDTO> authorize = authorizationService.authorize(username, tenantIds);
            //map-resources key:租户id  value:资源
//            Map<Long, List<ResourceDTO>> resources = CollectionUtils.isEmpty(authorize) ? null : resourceService.loadResources(authorize);
//            loginInfo.setResources(resources);
            loginInfo.setTenantAuthorizationInfoMap(authorize);
        }
        return loginInfo;
    }


    /**
     * 根据接入客户端查询应用
     *
     * @param clientId
     * @return
     */
    @Override
    public ApplicationDTO getApplicationDTOByClientId(String clientId) {
        QueryWrapper<ResourceApplication> queryWrapper = new QueryWrapper<ResourceApplication>();
        queryWrapper.lambda().eq(ResourceApplication::getCode, clientId);

        ResourceApplication resourceApplication = resourceApplicationMapper.selectOne(queryWrapper);

        return ResourceApplicationConvert.INSTANCE.entity2dto(resourceApplication);
    }

    @Override
    public TenantDTO getTenantByAccount(Long accountId, Boolean isAdmin) {
        TenantDTO tenant = null;
        //同一个手机号的账号, 只能作为一个租户的管理员
        TenantAccount tenantAccount = tenantAccountMapper.selectOne(new QueryWrapper<TenantAccount>().lambda()
                .eq(TenantAccount::getAccountId, accountId).eq(TenantAccount::getIsAdmin, isAdmin));
        if (tenantAccount != null && tenantAccount.getTenantId() != null) {
            tenant = getTenant(tenantAccount.getTenantId());
        }
        return tenant;
    }

    @Override
    public AccountRoleQueryDTO queryAccountRole(String username, String roleCode, Long tenantId) {
        AccountRoleQueryDTO accountRole = tenantAccountMapper.selectAccountRole(username, roleCode, tenantId);
        return accountRole;
    }

    @Override
    public PageVO<AccountRoleQueryDTO> queryAdministratorByPage(AccountRoleDTO query, Integer pageNo, Integer pageSize) {
        if (null == query.getTenantId()) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }
        Page<AccountRoleQueryDTO> page = new Page<>(pageNo, pageSize);// 当前页, 总条数 构造 page 对象
        List<AccountRoleQueryDTO> accountRole = tenantMapper.queryAdministratorByPage(page, query);
        return new PageVO<>(accountRole, page.getTotal(), pageNo, pageSize);
    }


    ///**
    // * 根据账号id和身份查询租户和账号是否绑定关系
    // * @param accountId
    // * @param isAdmin
    // * @return
    // */
    //@Override
    //public boolean queryTenantAccount(Long accountId, Boolean isAdmin) {
    //    Integer count = tenantAccountMapper.selectCount(new QueryWrapper<TenantAccount>().lambda()
    //            .eq(TenantAccount::getAccountId, accountId).eq(TenantAccount::getIsAdmin, isAdmin));
    //
    //    return count>0;
    //}


}
