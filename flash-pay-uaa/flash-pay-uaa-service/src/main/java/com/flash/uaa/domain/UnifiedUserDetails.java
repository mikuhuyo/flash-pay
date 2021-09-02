package com.flash.uaa.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UnifiedUserDetails implements UserDetails {

    private static final long serialVersionUID = 3957586021470480642L;
    /**
     * 用户名
     */
    private final String username;
    /**
     * 用户密码
     */
    private final String password;
    /**
     * 用户的授权集合
     */
    protected List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户附加信息,json字符串,统一认证透传
     */
    private Map<Long, Object> payload = new HashMap<>();

    /**
     * 接入应用所属租户
     */
    private Map<String, Object> tenant = new HashMap<>();


    public UnifiedUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public UnifiedUserDetails(String username, String password, Map<Long, Object> payload) {
        this.username = username;
        this.password = password;
        this.payload = payload;
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Map<Long, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<Long, Object> payload) {
        this.payload = payload;
    }


    /**
     * 账户是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用,默认true (启用)
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, Object> getTenant() {
        return tenant;
    }

    public void setTenant(Map<String, Object> tenant) {
        this.tenant = tenant;
    }
}
