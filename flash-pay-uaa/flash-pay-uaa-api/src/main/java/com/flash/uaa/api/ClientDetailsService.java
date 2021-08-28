package com.flash.uaa.api;

/**
 * @author yuelimin
 */
public interface ClientDetailsService {
    /**
     * 创建接入客户端, 用于用户中心应用的关联创建
     */
    void createClientDetails();
}
