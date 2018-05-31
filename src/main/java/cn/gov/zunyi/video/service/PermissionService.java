package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.Permission;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.gov.zunyi.video.mapper.PermissionMapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z77z
 * @since 2017-02-10
 */
@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

}
