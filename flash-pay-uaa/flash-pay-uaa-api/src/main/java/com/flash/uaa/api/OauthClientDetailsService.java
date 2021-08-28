package com.flash.uaa.api;

import java.util.Map;

/**
 * @author yuelimin
 */
public interface OauthClientDetailsService {

    /**
     * 添加客户端详细
     *
     * @param map 数据
     */
    void createClientDetails(Map<String, Object> map);

    /**
     * 根据appId查询 client信息
     *
     * @param appId client_id
     * @return Map<String, Object>
     */
    Map<String, Object> getClientDetailsByClientId(String appId);
}
