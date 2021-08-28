package com.flash.uaa.service;

import com.alibaba.fastjson.JSON;
import com.flash.uaa.api.OauthClientDetailsService;
import com.flash.uaa.common.utils.WebUtils;
import com.flash.uaa.domain.OauthClientDetails;
import com.flash.uaa.repository.OauthRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {

    @Autowired
    private OauthRepository oauthRepository;

    @Override
    public void createClientDetails(Map<String, Object> map) {
        log.info("OauthClientDetailsServiceImpl createClientDetails map: {}", map);

        OauthClientDetails clientDetails = new OauthClientDetails()
                .clientId(map.get("clientId").toString())
                .clientSecret(map.get("clientSecret").toString())
                .resourceIds("shanju-resource")
                .authorizedGrantTypes("client_credentials,password,authorization_code,implicit,refresh_token")
                .scope("read")
                .webServerRedirectUri(null)
                .authorities("ROLE_API")
                .accessTokenValidity(7200)
                .refreshTokenValidity(259200)
                .additionalInformation(null)
                .createTime(LocalDateTime.now())
                .archived(false)
                .trusted(false);

        oauthRepository.saveOauthClientDetails(clientDetails);
        log.info("{} | Save OauthClientDetails: {}", WebUtils.getIp(), clientDetails);
    }

    @Override
    public Map<String, Object> getClientDetailsByClientId(String appId) {
        OauthClientDetails oauthClientDetails = oauthRepository.findOauthClientDetails(appId);
        log.info("getClientDetailsByClientId param appId: {}, ret: {}", appId, JSON.toJSONString(oauthClientDetails));
        Map<String, Object> map = new HashMap<>();
        map.put("client_id", oauthClientDetails.clientId());
        map.put("client_secret", oauthClientDetails.clientSecret());
        return map;
    }
}
