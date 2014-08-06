package com.j7.transdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * <PRE>
 * History
 * 2004-7-21 21:04:00 created by Jem.Lee <李克喜>
 * 
 * 目的
 * 作为视图和控制器之间进行数据传递的主要对象，
 * 它作为FormData对象的元素对象
 * </PRE>
 */
@SuppressWarnings("rawtypes")
public class BsFormTable implements Serializable {

	private static final long serialVersionUID = -2702875799185906733L;

	/**
     * 
     */
	public BsFormTable() {
	}

	/*
	 * Table 名称
	 */
	public String name;

	/*
	 * 记录数组
	 */

	private ArrayList recs = new ArrayList();

	/*
	 * 存放该元素得父目录
	 */
	public BsFormData parent;

	/**
	 * <PRE>
	 * 构造函数 提供表名称
	 * @param initName 表名称
	 * </PRE>
	 */
	public BsFormTable(String initName) {
		/* 如果提供的表名为null，则抛出NullPointerException例外 */
		if (initName == null) {
			throw (new NullPointerException("构造FormTable时错误，比如表名为null"));
		}
		/* 如果提供的表名为空，则抛出NullPointerException例外 */
		if (initName.equals("")) {
			throw (new IllegalArgumentException("构造FormTable时错误，比如表名为空"));
		}
		this.name = initName;
	}

	/**
	 * <PRE>
	 * 设置元素的父目录
	 * @param newParent 父目录
	 * </PRE>
	 */
	void setParent(BsFormData newParent) throws NullPointerException {
		/* 如果提供的父目录为null，则抛出NullPointerException例外 */
		if (newParent == null) {
			throw (new NullPointerException("设置ForFormTablemField的包容类FormData错误，比如为null"));
		}
		this.parent = newParent;
	}

	/**
	 * 
	 * <PRE>
	 * 加入一条记录
	 * @return 返回一个记录项目
	 * </PRE>
	 */
	@SuppressWarnings("unchecked")
	public BsFormData add() {
		BsFormRecord newRec = new BsFormRecord();
		newRec.setParent(this);
		recs.add(newRec);
		this.reIndex();
		this.sort();
		return (newRec);
	}

	/**
	 * 
	 * <PRE>
	 * 排序函数，对表的记录进行排序
	 * </PRE>
	 */
	@SuppressWarnings("unchecked")
	void sort() {
		Collections.sort(recs);
		Iterator iRecs = recs.iterator();
		while (iRecs.hasNext()) {
			BsFormData rec = (BsFormData) iRecs.next();
			rec.sort();
		}
	}

	/**
	 * 
	 * <PRE>
	 * 记录索引
	 * </PRE>
	 */
	void reIndex() {
		int index = 0;
		// Collections.sort(recs);
		Iterator i = recs.iterator();
		while (i.hasNext()) {
			BsFormRecord rec = (BsFormRecord) i.next();
			rec.setRecId(index);
			index++;
		}
	}

	/**
	 * 
	 * <PRE>
	 * 通过记录号获得一条记录
	 * @param recNo 记录号 
	 * @return 一条记录
	 * </PRE>
	 */
	public BsFormData get(int recNo) {
		return ((BsFormData) recs.get(recNo));
	}

	/**
	 * 
	 * <PRE>
	 * 获得表的长度
	 * @return 表的长度
	 * </PRE>
	 */
	public int size() {
		return (recs.size());
	}

}
