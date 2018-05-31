package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.Role;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.gov.zunyi.video.mapper.RoleMapper;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

}
