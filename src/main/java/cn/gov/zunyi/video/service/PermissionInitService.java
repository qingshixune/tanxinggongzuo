package cn.gov.zunyi.video.service;

import java.util.List;

import cn.gov.zunyi.video.model.PermissionInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.gov.zunyi.video.mapper.PermissionInitMapper;

@Service
public class PermissionInitService extends ServiceImpl<PermissionInitMapper, PermissionInit> {

	@Autowired
	private PermissionInitMapper permissionInitMapper;

	public List<PermissionInit> selectAll() {
		return permissionInitMapper.selectAll();
	}
}
