package cn.gov.zunyi.video.mapper;

import java.util.List;

import cn.gov.zunyi.video.model.PermissionInit;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface PermissionInitMapper extends BaseMapper<PermissionInit> {

	List<PermissionInit> selectAll();

}