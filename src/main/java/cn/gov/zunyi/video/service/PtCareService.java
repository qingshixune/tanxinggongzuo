package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.PtCare;
import cn.gov.zunyi.video.mapper.PtCareMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PtCareService extends ServiceImpl<PtCareMapper,PtCare> {

    @Autowired
    private PtCareMapper ptCareMapper;

    public List<PtCare> query(Page<PtCare> page, int status) {
        return ptCareMapper.query(page,status);
    }
}
