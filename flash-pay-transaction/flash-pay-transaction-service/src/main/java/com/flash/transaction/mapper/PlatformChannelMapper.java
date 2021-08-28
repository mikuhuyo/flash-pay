package com.flash.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flash.transaction.api.dto.PayChannelDto;
import com.flash.transaction.entity.PlatformChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Repository
public interface PlatformChannelMapper extends BaseMapper<PlatformChannel> {
    /**
     * 根据平台服务类型查询原始支付渠道
     *
     * @param platformChannelCode 平台渠道码
     * @return List<PayChannelDTO>
     */
    @Select("select pay.* from pay_channel pay, platform_pay_channel pac, platform_channel pla "
            + "where pay.CHANNEL_CODE = pac.PAY_CHANNEL "
            + "and pla.CHANNEL_CODE = pac.PLATFORM_CHANNEL "
            + "and pla.CHANNEL_CODE = #{code}")
    List<PayChannelDto> selectPayChannelByPlatformChannel(@Param("code") String platformChannelCode);
}
