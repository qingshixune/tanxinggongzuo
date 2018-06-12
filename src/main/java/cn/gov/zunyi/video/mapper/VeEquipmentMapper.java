package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.VeEquipment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VeEquipmentMapper extends BaseMapper<VeEquipment> {
    List<VeEquipment> selectVeAll(Page<VeEquipment> page);

    List<VeEquipment> selectVeAllByType(Page<VeEquipment> page, Integer veType);

    List<VeEquipment> getVeList(Page<VeEquipment> page, @Param("typeids") List<String> typeids,
                                @Param("addressid") int addressid,
                                @Param("veStatus") int veStatus,
                                @Param("beforeDate") String beforeDate,
                                @Param("afterDate") String afterDate,
                                @Param("veNames") String[] veNames);
}
