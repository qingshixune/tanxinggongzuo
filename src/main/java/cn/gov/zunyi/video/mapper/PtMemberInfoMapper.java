package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.PtMemberInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PtMemberInfoMapper extends BaseMapper<PtMemberInfo> {
    List<PtMemberInfo> getPartyInfo(@Param("beforeYear") String beforeYear, @Param("afterYear") String afterYear);

    PtMemberInfo getPartyInfoByTime(@Param("year") int year, @Param("month") int month);
}
