package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.PtCare;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PtCareMapper extends BaseMapper<PtCare> {
    List<PtCare> query(Page<PtCare> page, @Param("status") int status);
}
