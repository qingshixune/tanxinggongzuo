package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.mapper.VideoMapper;
import cn.gov.zunyi.video.model.VeCount;
import cn.gov.zunyi.video.model.Video;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoService extends ServiceImpl<VideoMapper,Video> {
    @Autowired
    private VideoMapper videoMapper;

    public List<Video> getVideoByAddress(Page<Video> page, String veAddress) {
        return videoMapper.getVideoByAddress(page,veAddress);
    }

    public List<Video> getVideoById(Page<Video> page, String id) {
        return videoMapper.getVideoById(page,id);
    }


    public List<Video> getVideoByName(Page<Video> page, String veName) {
        return videoMapper.getVideoByName(page,veName);
    }

    public VeCount getVeCount(List<Video> videos) {
        VeCount veCount = new VeCount();
        for (Video video:videos ) {
            veCount.setVideoLongs(veCount.getVideoLongs()+video.getVideoLong());
        }
        return veCount;
    }
}
