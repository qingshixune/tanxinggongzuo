package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.mapper.VeEventFlowMapper;
import cn.gov.zunyi.video.model.VeEventFlow;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VeEventFlowService extends ServiceImpl<VeEventFlowMapper,VeEventFlow> {
    @Autowired
    private VeEventFlowMapper veEventFlowMapper;

    public VeEventFlow selectByEid(String id) {
        return veEventFlowMapper.selectByEid(id);
    }
}
