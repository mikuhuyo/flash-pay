package com.flash.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flash.user.entity.Bundle;
import org.springframework.stereotype.Repository;

@Repository
public interface BundleMapper extends BaseMapper<Bundle> {

    // @Select("select * from bundle where CODE=#{bundleCode}")
    // Bundle selectBundleByCode(@Param("bundleCode") String bundleCode);

}
