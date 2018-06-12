package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.VeEquipmentRunstus;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface VeEquipmentRunstusMapper extends BaseMapper<VeEquipmentRunstus> {
    List<VeEquipmentRunstus> getVeEquipmentRunstusByEqid(String eqid);
}
