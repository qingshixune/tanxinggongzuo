package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.VeEventFlow;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface VeEventFlowMapper extends BaseMapper<VeEventFlow> {
    VeEventFlow selectByEid(String eid);
}
