package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.mapper.EmergencyCommandMapper;
import cn.gov.zunyi.video.model.EmergencyCommand;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmergencyCommandService extends ServiceImpl<EmergencyCommandMapper,EmergencyCommand> {
}
