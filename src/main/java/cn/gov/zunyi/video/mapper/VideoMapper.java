package cn.gov.zunyi.video.mapper;

import cn.gov.zunyi.video.model.Video;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoMapper extends BaseMapper<Video> {
    List<Video> getVideoByAddress(Page<Video> page,@Param("veAddress") String veAddress);

    List<Video> getVideoById(Page<Video> page,@Param("id") String id);

    List<Video> getVideoByName(Page<Video> page,@Param("veName") String veName);

    List<Video> getVideo(Page<Video> page,
                         @Param("typeids") List<String> typeids,
                         @Param("addressid") int addressid,
                         @Param("veStatus") int veStatus);
}
