package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.News;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.gov.zunyi.video.mapper.NewsMapper;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> {

}
