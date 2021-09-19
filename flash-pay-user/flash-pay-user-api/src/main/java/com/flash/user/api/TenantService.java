package com.flash.user.api;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.PageVO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.tenant.*;

import java.util.List;

/**
 * 租户服务: 负责租户, 套餐, 账号等信息维护
 * <p>
 * 租户类型, 套餐为手工维护
 */
public interface TenantService {

    /**
     * 在租户内创建账号并绑定角色, 包含校验账号是否存在及角色是否绑定
     *
     * @param tenantId
     * @param accountRequest
     * @param roleCodes
     */
    void checkCreateStaffAccountRole(Long tenantId, CreateAccountRequestDTO accountRequest, String[] roleCodes);

    /**
     * 更新账号的角色信息
     *
     * @param username
     * @param tenantId
     * @param roleCodes
     */
    void getAccountRoleBind(String username, Long tenantId, String[] roleCodes);

    /**
     * 创建租户
     * 新增租户, 新增租户管理员, 同时初始化套餐
     * 1.若管理员用户名已存在(目前不会出现, 用户名内部随机生成)
     * 2.手机号已存在, 禁止创建
     *
     * @param createTenantRequest 创建租户请求信息
     * @return
     */
    TenantDTO createTenantAndAccount(CreateTenantRequestDTO createTenantRequest);


    /**
     * 创建租户
     * 新增租户, 管理到对应管理员, 同时初始化套餐
     * 1.若管理员用户名已存在(目前不会出现, 用户名内部随机生成)
     * 2.手机号已存在, 禁止创建
     *
     * @param createTenantRequest 创建租户请求信息
     * @return
     */
    TenantDTO createTenantRelateAccount(CreateTenantRequestDTO createTenantRequest);


    /**
     * 删除租户
     * 解绑租户下所有账号, 清除租户下账号-角色信息, 清除租户下角色, 删除租户
     *
     * @param id 租户id
     */
    void removeTenant(Long id) throws BusinessException;


    /**
     * 获取租户信息
     *
     * @param id
     * @return 租户信息
     */
    TenantDTO getTenant(Long id) throws BusinessException;


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
    PageVO<TenantDTO> queryTenants(TenantQueryDTO tenantQuery, Integer pageNo, Integer pageSize, String sortBy,
                                   String order) throws BusinessException;


    /**
     * 查询某租户类型的套餐列表(不包含初始化套餐)
     *
     * @param tenantType
     * @return
     */
    List<BundleDTO> queryBundleByTenantType(String tenantType) throws BusinessException;

    ////////////////////套餐管理start/////////////////////

    /**
     * 获取某套餐信息
     *
     * @param bundleCode 套餐编码
     * @return 套餐信息
     */
    BundleDTO getBundle(String bundleCode);

    /**
     * 查询所有套餐
     *
     * @return
     */
    List<BundleDTO> queryAllBundle();

    /**
     * 切换租户套餐
     * 租户切换套餐操作会清除原租户内的所有账号-角色关联数据以及原套餐产生的角色权限数据,并将限流规则写入sentinel
     *
     * @param tenantId   租户id
     * @param bundleCode 套餐编码
     */
    void changeBundle(Long tenantId, String bundleCode) throws BusinessException;

    /**
     * 初始化租户套餐
     * 将限流规则写入sentinel
     *
     * @param tenantId   租户id
     * @param bundleCode 套餐编码
     */
    void initBundle(Long tenantId, String bundleCode) throws BusinessException;

    /**
     * 新增套餐
     *
     * @param bundleDTO
     */
    void createBundle(BundleDTO bundleDTO) throws BusinessException;

    /**
     * 更新套餐
     *
     * @param bundleDTO
     */
    void modifyBundle(BundleDTO bundleDTO) throws BusinessException;
    ////////////////////套餐管理end/////////////////////


    /**
     * 创建账号
     * 1.若用户名已存在(目前不会出现, 用户名内部随机生成), 禁止创建
     * 2.手机号已存在, 禁止创建
     *
     * @param createAccountRequest 创建账号请求
     */
    AccountDTO createAccount(CreateAccountRequestDTO createAccountRequest) throws BusinessException;

    /**
     * 修改账号密码
     *
     * @param accountRequest
     * @return
     */
    boolean accountPassword(ChangeAccountPwdDTO accountRequest);

    /**
     * 创建账号并绑定至某租户
     * 1.若用户名已存在(目前不会出现, 用户名内部随机生成), 禁止创建
     * 2.手机号已存在, 禁止创建
     *
     * @param createAccountRequest 创建账号请求
     * @param tenantId             租户id
     */
    void createAccountInTenant(CreateAccountRequestDTO createAccountRequest, Long tenantId) throws
            BusinessException;

    /**
     * 将已存在账号, 加入到某租户(绑定关系, 身份不是管理员)
     * 界面上先通过手机号查询到账号信息, 再通过账号信息的username, 调用此接口, 加入租户
     *
     * @param tenantId 租户id
     * @param username 用户名
     */
    void bindTenant(Long tenantId, String username) throws BusinessException;

    ///**
    // * 注册时, 管理员账号绑定到租户
    // * @param tenantId
    // * @param accountId
    // * @param isAdmin
    // * @throws BusinessException
    // */
    //void bindTenantAccount(Long tenantId, Long accountId,Boolean isAdmin) throws BusinessException;

    /**
     * 将某账号从租户内移除, 租户管理员不可移除
     *
     * @param tenantId 租户id
     * @param username 用户名
     */
    void unbindTenant(Long tenantId, String username) throws BusinessException;


    /**
     * 根据用户名判断账号是否存在
     *
     * @param username 用户名
     * @return
     */
    boolean isExistAccountByUsername(String username) throws BusinessException;

    /**
     * 根据手机号判断账号是否存在
     *
     * @param mobile 手机号
     * @return
     */
    boolean isExistAccountByMobile(String mobile) throws BusinessException;


    /**
     * 根据用户名获取账号信息
     *
     * @param username 用户名
     * @return 账号信息
     */
    AccountDTO getAccountByUsername(String username) throws BusinessException;

    /**
     * 根据手机号获取账号信息
     *
     * @param mobile 手机号
     * @return 账号信息
     */
    AccountDTO getAccountByMobile(String mobile) throws BusinessException;


    /**
     * 根据用户名判断账号是否在某租户内
     *
     * @param tenantId 租户id
     * @param username 用户名
     * @return
     */
    boolean isExistAccountInTenantByUsername(Long tenantId, String username) throws BusinessException;

    /**
     * 根据手机号判断账号是否在某租户内
     *
     * @param tenantId 租户id
     * @param mobile   手机号
     * @return
     */
    boolean isExistAccountInTenantByMobile(Long tenantId, String mobile) throws BusinessException;

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
    PageVO<AccountDTO> queryAccount(AccountQueryDTO accountQuery, Integer pageNo, Integer pageSize, String sortBy, String order) throws BusinessException;

    /**
     * 查询某账号所属租户列表
     *
     * @param username 用户名
     * @return
     */
    List<TenantDTO> queryAccountInTenant(String username) throws BusinessException;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    String sendMessage(String phone);

    /**
     * 用户认证
     *
     * @param authenticationInfo 认证请求信息
     * @return 认证成功的账号信息
     */
    AccountDTO authentication(AuthenticationInfo authenticationInfo) throws BusinessException;


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
    LoginInfoDTO login(LoginRequestDTO loginRequest) throws BusinessException;

    /**
     * 根据接入客户端获取应用
     *
     * @param clientId
     * @return
     * @throws BusinessException
     */
    ApplicationDTO getApplicationDTOByClientId(String clientId) throws BusinessException;

    /**
     * 根据账号和身份获取租户
     *
     * @param accountId
     * @param isAdmin
     * @return
     * @throws BusinessException
     */
    public TenantDTO getTenantByAccount(Long accountId, Boolean isAdmin) throws BusinessException;

    /**
     * 查询某个管理员
     *
     * @param username
     * @param roleCode
     * @param tenantId
     * @return
     */
    AccountRoleQueryDTO queryAccountRole(String username, String roleCode, Long tenantId);

    /**
     * 分页条件查询管理员
     *
     * @param accountRoleDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageVO<AccountRoleQueryDTO> queryAdministratorByPage(AccountRoleDTO accountRoleDTO, Integer pageNo,
                                                         Integer pageSize);
    ///**
    // * 根据账号id和身份查询租户和账号是否绑定关系
    // * @param accountId
    // * @param isAdmin
    // * @return
    // */
    //boolean queryTenantAccount(Long accountId, Boolean isAdmin) throws BusinessException;

}
