package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * <p>
 * 事件上报方式类
 * </p>
 *
 * @author ZTY
 * @since 2018-6-6
 */
@ApiModel("事件上报方式")
@TableName("ve_event_method")
public class VeEventMethod extends Model<VeEventMethod> {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
