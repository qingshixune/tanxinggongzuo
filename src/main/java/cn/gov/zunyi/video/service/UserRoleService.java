package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.UserRole;
import org.springframework.stereotype.Service;

import cn.gov.zunyi.video.mapper.UserRoleMapper;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

}
