package org.sam.bufferframedb;

import java.io.Serializable;

/**
 * 文件结构元数据对象
 * 
 * @author sam
 *
 */
public class Table implements Serializable , Cloneable {

	private static final long serialVersionUID = 3325731818695303988L;
	
	/**
	 * 字段类型
	 */
	private Field[] fields;

	/**
	 * 获取字段类型的列表
	 * 
	 * @return
	 */
	public Field[] getFields() {
		return fields;
	}

	/**
	 * 设置字段类型的列表
	 * 
	 * @param fields
	 */
	private void setFields(Field[] fields) {
		this.fields = fields;
	}

	/**
	 * 对应的名称
	 */
	private String name;

	/**
	 * 表名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置表名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 初始化表数据结构 一旦初始化不允许修改
	 * 
	 * @param name
	 *            名称
	 * @param fields
	 *            字段列表
	 */
	public Table(String name, Field... fields) {
		this.setFields(fields);
		this.setName(name);
	}
	
	/**
	 * 获取存储的总长度
	 * @return
	 */
	public int getSize(){
		if (this.fields == null || this.fields.length <= 0)
			return 0;
		
		int iSum = 0;
		for(Field field : this.fields){
			iSum += field.getLenth() * field.getDataType().getBaseSize();
		}
		
		return iSum;
	}

	/**
	 * clone a table metadata
	 */
	@Override
	protected Table clone() throws CloneNotSupportedException {
		return (Table)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{table:");
		sb.append(this.getName());
		sb.append(";fields:[");
		
		for(Field field : this.fields){
			sb.append(field.getDataType().toString());
			sb.append(",length:");
			sb.append(field.getLenth());
			sb.append("],");
		}
		
		sb.append("]}");
		return sb.toString();
	}
	
	
}
