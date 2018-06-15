package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.ElevatorEvent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ElevatorEventMapper extends BaseMapper<ElevatorEvent> {
    List<ElevatorEvent> selectEvent(Page<ElevatorEvent> page,
                                    @Param("id") String id,
                                    @Param("eventStatus")Integer eventStatus,
                                    @Param("typeids")List typeids,
                                    @Param("beforeDate")String beforeDate,
                                    @Param("afterDate")String afterDate);
}
