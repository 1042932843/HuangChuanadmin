package com.admin.huangchuan.model;
/**
 * Element类
 */
public class Element {
	/** 文字内容 */
	private String contentText;
	/** 在tree中的层级 */
	private String level;
	/** 元素的id */
	private String id;
	/** 父元素的id */
	private String parendId;
	/** 是否有子元素 */
	private String hasChildren;
	/** item是否展开 */
	private String isExpanded;
	
	/** 表示该节点没有父元素，也就是level为0的节点 */
	public static final int NO_PARENT = -1;
	/** 表示该元素位于最顶层的层级 */
	public static final int TOP_LEVEL = 1;

	/** 党支部名字 */
	private String showArea;//(1不显示党支部，2显示党支部)

	public String getShowArea() {
		return showArea;
	}

	public void setShowArea(String showArea) {
		this.showArea = showArea;
	}

	/*	public Element(String contentText, int level, int id, int parendId,
				   boolean hasChildren, boolean isExpanded) {
		super();
		this.contentText = contentText;
		this.level = level;
		this.id = id;
		this.parendId = parendId;
		this.hasChildren = hasChildren;
		this.isExpanded = isExpanded;
	}*/

	public String isExpanded() {
		return isExpanded;
	}

	public void setExpanded(String isExpanded) {
		this.isExpanded = isExpanded;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParendId() {
		return parendId;
	}

	public void setParendId(String parendId) {
		this.parendId = parendId;
	}

	public String isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}



}
