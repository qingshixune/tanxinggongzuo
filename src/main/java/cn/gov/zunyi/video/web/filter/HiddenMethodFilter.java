package cn.gov.zunyi.video.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * 但是需要把form表单的enctype属性设置为application/x-www-form-urlencoded
 * 
 * @author Danny Lee
 */
@Component
public class HiddenMethodFilter extends HiddenHttpMethodFilter {
}