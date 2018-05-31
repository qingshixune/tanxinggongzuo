package cn.gov.zunyi.video.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class TreeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String text;

	@ApiModelProperty(hidden = true)
	private List<TreeInfo> children = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeInfo> getChildren() {
		return children;
	}

}