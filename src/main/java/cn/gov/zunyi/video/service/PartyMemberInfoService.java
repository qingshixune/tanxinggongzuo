package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.PartyMemberInfo;
import cn.gov.zunyi.video.mapper.PartyMemberInfoMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyMemberInfoService extends ServiceImpl<PartyMemberInfoMapper, PartyMemberInfo> {

    @Autowired
    private PartyMemberInfoMapper partyMemberInfoMapper;


    public Page<PartyMemberInfo> getPartyBranchName(Page<PartyMemberInfo> page) {
        page.setRecords(partyMemberInfoMapper.getPartyBranchName(page));
        return page;
    }

    public List<PartyMemberInfo> getPartyMemberSexCount() {
        return partyMemberInfoMapper.getPartyMemberSexCount();
    }

    public List<PartyMemberInfo> getPartyMemberAgeDistribution() {
        return partyMemberInfoMapper.getPartyMemberAgeDistribution();
    }

    public PartyMemberInfo getPmInfoByMemberId(String memberId){
        return partyMemberInfoMapper.getPmInfoByMemberId(memberId);
    }
}
