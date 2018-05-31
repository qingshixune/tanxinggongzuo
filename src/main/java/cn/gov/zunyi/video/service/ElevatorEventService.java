package cn.gov.zunyi.video.service;


import cn.gov.zunyi.video.mapper.ElevatorEventMapper;
import cn.gov.zunyi.video.model.ElevatorEvent;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElevatorEventService extends ServiceImpl<ElevatorEventMapper,ElevatorEvent>{
    @Autowired
    private ElevatorEventMapper elevatorEventMapper;
}
