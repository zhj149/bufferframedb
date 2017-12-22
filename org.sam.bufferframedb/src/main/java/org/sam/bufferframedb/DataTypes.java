package org.sam.bufferframedb;

/**
 * 本系统支持的数据类型
 * 8种基础类型 + 字符串
 * @author sam
 *
 */
public enum DataTypes {

	/**
	 * 1 byte
	 */
	BYTE(Byte.BYTES),
	/**
	 * int16 2 bytes
	 */
	SHORT(Short.BYTES),
	/**
	 * utf-8 char 3 bytes
	 */
	CHAR(Character.BYTES),
	/**
	 * int32 4 bytes
	 */
	INTEGER(Integer.BYTES),
	/**
	 * int64 8 bytes
	 */
	LONG(Long.BYTES),
	/**
	 * boolean 1 bytes, in jvm 1 boolean is 4byte , boolean-array 1byte foreach
	 * i use 1 byte
	 */
	BOOLEAN(Byte.BYTES),
	/**
	 * float 4 bytes
	 */
	FLOAT(Float.BYTES),
	/**
	 * double 8 bytes
	 */
	DOUBLE(Double.BYTES),
	/**
	 * char-array 2 bytes base
	 */
	CHARS(Character.BYTES);
	
	private int baseSize;
	
	/**
	 * 本系统支持的数据类型
	 * @param baseSize 基本类型长度
	 */
	private DataTypes(int baseSize){
		this.baseSize = baseSize;
	}

	/**
	 * 占用字节大小
	 * @return
	 */
	public int getBaseSize() {
		return baseSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "{name:" + this.name() + ",size:" + this.getBaseSize() + "}";
	}
}
