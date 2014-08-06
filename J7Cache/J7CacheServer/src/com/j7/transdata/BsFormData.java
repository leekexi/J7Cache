package com.j7.transdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <PRE>
 * History
 * 2004-7-21 21:02:57 created by Jem.Lee <李克喜>
 * 
 * 目的
 * 作为视图和控制器之间进行数据传递的主要对象， 
 * 它包涵一般的元素对象和Table对象
 * </PRE>
 */
@SuppressWarnings("rawtypes")
public class BsFormData implements Cloneable, Serializable {

	private static final long serialVersionUID = -484641233264066361L;

	public BsFormData() {
	}

	/*
	 * 存放单个元素的Hash表
	 */

	private HashMap elements = new HashMap();
	/*
	 * 存放Table元素的的Hash表
	 */
	private HashMap tables = new HashMap();

	/**
	 * <PRE>
	 * 从FormData获得指定元素名称的值
	 * @param name 元素名称
	 * @return 元素对应的值
	 * @throws Exception
	 * </PRE>
	 */
	public String get(String name) throws Exception {
		return (this.getField(name).getValue());
	}

	/**
	 * <PRE>
	 * 从FormData获得指定元素域名称
	 * @param name 元素名称
	 * @return 元素对应的域
	 * @throws Exception
	 * </PRE>
	 */
	public BsFormField getField(String name) throws Exception {
		if (name == null) {
			throw (new NullPointerException("输入的域名为空或是null"));
		}
		BsFormField ret = (BsFormField) elements.get(name);
		if (ret == null) {
			throw (new Exception("找不到对应'" + name + "'域名"));
		}
		return (ret);
	}

	/**
	 * <PRE>
	 * 给FormData指定一个元素并设置它的值，如果发现该元素已经存在，则覆盖该元素。
	 * @param name  元素的名称
	 * @param value 元素的值
	 * </PRE>
	 * 
	 * @throws Exception
	 */
	public void putForce(String name, String value) throws Exception {
		try {
			if (value == null) {
				value = "";
			}
			BsFormField newElement = new BsFormField(name, value);
			newElement.setParent(this);
			this.putElementForce(newElement);
			newElement = null;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * <PRE>
	 * 设置FormData的Elevent域。
	 * @param newElement 指定的新域
	 * </PRE>
	 */
	@SuppressWarnings("unchecked")
	public void putElementForce(BsFormField newElement) {
		newElement.setParent(this);
		this.elements.put(newElement.getName(), newElement);
	}

	/*------------------------------Table------------------*/

	/**
	 * <PRE>
	 * 增加Table到FormData中，并返回该Table的实例,如果指定的表已经存在，
	 * 则抛出BsComponentException例外
	 * @param name  Table的名称
	 * @return FormTable  所返回的Table类型
	 * @throws BsComponentException
	 * </PRE>
	 */
	public BsFormTable addTable(String name) throws Exception {
		if (tables.get(name) != null) {
			throw (new Exception());
		}
		return (addTableForce(name));
	}

	/**
	 * <PRE>
	 * 增加Table到FormData中，并返回该Table的实例，如果该指定的表已经存在，
	 * 则强行覆盖新的表。
	 * @param tableName  Table的名称
	 * @return FormTable  所返回的Table类型
	 * </PRE>
	 */
	@SuppressWarnings("unchecked")
	public BsFormTable addTableForce(String tableName) {
		BsFormTable newTable = new BsFormTable(tableName);
		newTable.setParent(this);
		tables.put(tableName, newTable);
		return (newTable);
	}

	/**
	 * <PRE>
	 * 对Table的内容进行排序
	 * </PRE>
	 */
	void sort() {
		Iterator iTables = this.tables.keySet().iterator();
		while (iTables.hasNext()) {
			BsFormTable tbl = (BsFormTable) this.tables.get(iTables.next());
			tbl.sort();
		}
	}

	/**
	 * <PRE>
	 * 获得指定表名称的实例
	 * @param name  表名称
	 * @return 指定表名称的实例
	 * @throws BsComponentException、NullPointerException、IllegalArgumentException
	 * </PRE>
	 */
	public BsFormTable getTable(String name) throws Exception {
		if (name == null) {
			throw (new NullPointerException("输入的表名是null"));
		}
		if (name.equals("")) {
			throw (new IllegalArgumentException("输入的域名为空"));
		}
		BsFormTable ret = (BsFormTable) tables.get(name);
		if (ret == null) {
			throw (new Exception());
		}
		return (ret);
	}
}
