package com.j7.transdata;

import java.io.Serializable;

/**
 * <PRE>
 * History
 * 2004-7-21 21:03:44 created by Jem.Lee <李克喜>
 * 
 * 目的
 * 作为视图和控制器之间进行数据传递的主要对象，
 * 它作为Table对象的元素对象
 * </PRE>
 */
@SuppressWarnings("rawtypes")
public class BsFormRecord extends BsFormData implements Comparable, Serializable {

	private static final long serialVersionUID = -7567691930469190427L;

	public BsFormRecord() {
	}

	/*
	 * 记录 ID
	 */
	private int recId;

	/*
	 * 存放该元素得父目录
	 */
	private BsFormTable parent;

	public int compareTo(Object target) {
		if (target instanceof BsFormRecord) {
			return (this.recId - ((BsFormRecord) target).getRecId());
		}
		return (0);
	}

	/**
	 * <PRE>
	 * 设置元素的父目录
	 * @param newParent 父目录
	 * </PRE>
	 */
	void setParent(BsFormTable newParent) {
		this.parent = newParent;
	}

	/**
	 * <PRE>
	 * 获得元素的父目录
	 * @return FormTable 父目录
	 * </PRE>
	 */
	BsFormTable getParent() {
		return (parent);
	}

	/**
	 * 
	 * <PRE>
	 * 设置 新的记录ID
	 * @param newId 记录ID
	 * </PRE>
	 */
	void setRecId(int newId) {
		this.recId = newId;
	}

	/**
	 * 
	 * <PRE>
	 * 获得的记录ID
	 * @return 记录ID
	 * </PRE>
	 */
	int getRecId() {
		return (recId);
	}
}
