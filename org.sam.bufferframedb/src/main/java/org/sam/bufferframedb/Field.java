package org.sam.bufferframedb;

import java.io.Serializable;

/**
 * 字段类型
 * 
 * @author sam
 *
 */
public class Field implements Serializable, Cloneable {

	private static final long serialVersionUID = 4006502080591354078L;

	/**
	 * 唯一标示，不重复
	 */
	private int id;

	/**
	 * 基础数据类型
	 */
	private DataTypes dataType;

	/**
	 * 字段长度 基础类型默认1
	 */
	private int lenth;

	/**
	 * 唯一标示对象
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 唯一标示对象
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 基础数据类型
	 * 
	 * @return
	 */
	public DataTypes getDataType() {
		return dataType;
	}

	/**
	 * 基础数据类型
	 * 
	 * @param dataType
	 */
	public void setDataType(DataTypes dataType) {
		this.dataType = dataType;
	}

	/**
	 * 字段长度 基础类型默认1
	 * 
	 * @return
	 */
	public int getLenth() {
		return lenth;
	}

	/**
	 * 字段长度 基础类型默认1
	 * 
	 * @param lenth
	 */
	public void setLenth(int lenth) {
		this.lenth = lenth;
	}

	/**
	 * 获取字段所占byte数组总长度
	 */
	public int getSize() {
		return this.lenth * this.dataType.getBaseSize();
	}

	/**
	 * 初始化字段对象
	 * 
	 * @param id
	 *            标示
	 * @param dataType
	 *            数据类型
	 * @param length
	 *            长度
	 */
	public Field(int id, DataTypes dataType, int length) {
		this.setId(id);
		this.setDataType(dataType);
		this.setLenth(length);
	}

	/**
	 * 初始化字段对象 默认长度1
	 * 
	 * @param id
	 *            标示
	 * @param dataType
	 *            数据类型
	 */
	public Field(int id, DataTypes dataType) {
		this(id, dataType, 1);
	}

	/**
	 * 初始化字段对象 默认int类型，1位
	 * 
	 * @param id
	 *            标示
	 */
	public Field(int id) {
		this(id, DataTypes.INTEGER, 1);
	}
}
