package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.PartyMemberInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartyMemberInfoMapper extends BaseMapper<PartyMemberInfo>{

    List<PartyMemberInfo> getPartyBranchName(Page<PartyMemberInfo> page);

    List<PartyMemberInfo> getPartyMemberSexCount();

    List<PartyMemberInfo> getPartyMemberAgeDistribution();

    PartyMemberInfo getPmInfoByMemberId(@Param(value = "memberId") String memberId);

}
