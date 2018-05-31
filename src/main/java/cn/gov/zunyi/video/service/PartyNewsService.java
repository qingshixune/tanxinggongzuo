package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.News;
import cn.gov.zunyi.video.mapper.PartyNewsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 党政府新闻 Service(1.通知公告 2.党务公开 3.汇川先锋)
 *
 * @author yangzhiping
 * @Date: 2018/5/7 15:45
 */
@Service
public class PartyNewsService extends ServiceImpl<PartyNewsMapper, News> {
}
