package com.j7.transdata;

import java.io.Serializable;

/**
 * <PRE>
 * History
 * 2004-7-21 21:03:26 created by Jem.Lee <李克喜>
 * 
 * 目的
 * 作为视图和控制器之间进行数据传递的主要对象，
 * 它作为一般的元素对象
 * </PRE>
 */
public class BsFormField implements Serializable {

	private static final long serialVersionUID = -6890867458627036897L;

	/*
	 * 元素名称
	 */
	private String name;

	/*
	 * 元素值
	 */
	private String value;

	/*
	 * 存放该元素得父目录
	 */
	public BsFormData parent = null;;

	/**
	 * 构造函数
	 * 
	 * @param initName 元素名称
	 * @param initValue 元素值
	 */
	public BsFormField(String initName, String initValue) {

		/* 如果提供的元素名称为null则抛出NullPointerException例外 */
		if (initName == null) {
			throw (new NullPointerException("初始化FormField时提供的参数错误，比如其中initName为null"));
		}
		/* 如果提供的元素名称为空则抛出IllegalArgumentException例外 */
		if (initName.equals("")) {
			throw (new IllegalArgumentException("初始化FormField时提供的参数错误，initName为空"));
		}
		/* 如果提供的元素值为null则抛出NullPointerException例外 */
		if (initValue == null) {
			throw (new NullPointerException("初始化FormField时提供的参数错误，initValue为null"));
		}
		this.name = initName;
		this.value = initValue;
	}

	/**
	 * <PRE>
	 * 获得元素名称
	 * @return 元素名称
	 * </PRE>
	 */
	public String getName() {
		return (this.name);
	}

	/**
	 * <PRE>
	 * 获得元素的值
	 * @return 元素的值
	 * </PRE>
	 */
	public String getValue() {
		return (this.value);
	}

	/**
	 * <PRE>
	 * 设置元素的父目录
	 * @param newParent 父目录
	 * </PRE>
	 */
	public void setParent(BsFormData newParent) {
		/* 如果父目录为null，则抛出NullPointerException例外 */
		if (newParent == null) {
			throw (new NullPointerException("设置FormField的包容类FormData错误，比如为null"));
		}
		parent = newParent;
	}

}
