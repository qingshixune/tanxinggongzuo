package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.PartyBranchInfo;
import cn.gov.zunyi.video.mapper.PartyBranchInfoMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyBranchInfoService extends ServiceImpl<PartyBranchInfoMapper, PartyBranchInfo> {

    @Autowired
    private PartyBranchInfoMapper partyBranchInfoMapper;

}
