package cn.gov.zunyi.video.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.HttpPutFormContentFilter;

/**
 * 解决SpringBoot获取不到PUT方式提交的参数的问题,但是需要把form表单的enctype属性设置为application/x-www-form-urlencoded
 * 
 * @author Danny Lee
 */
@Component
public class PutFilter extends HttpPutFormContentFilter {
}