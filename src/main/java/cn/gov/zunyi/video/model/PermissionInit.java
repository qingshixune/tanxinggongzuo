package cn.gov.zunyi.video.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author Danny Lee
 * @since 2017-03-29
 */
@TableName("tb_permission_init")
public class PermissionInit extends Model<PermissionInit> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 链接地址
	 */
	private String url;
	/**
	 * 需要具备的权限
	 */
	@TableField("permission_init")
	private String permissionInit;
	/**
	 * 排序
	 */
	private Integer sortorder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermissionInit() {
		return permissionInit;
	}

	public void setPermissionInit(String permissionInit) {
		this.permissionInit = permissionInit;
	}

	public Integer getSortorder() {
		return sortorder;
	}

	public void setSortorder(Integer sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
