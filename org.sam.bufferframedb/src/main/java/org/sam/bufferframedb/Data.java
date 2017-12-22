package org.sam.bufferframedb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sam.bufferframedb.utils.ByteUtil;

/**
 * 数据存储对象
 * 
 * @author sam
 *
 */
public class Data implements Serializable, Cloneable {

	private static final long serialVersionUID = 8229845889697256195L;

	/**
	 * 数据对象
	 */
	private Map<Integer, Object> datas = new HashMap<>();

	/**
	 * 数据对象
	 * 
	 * @return
	 */
	public Map<Integer, Object> getDatas() {
		return datas;
	}

	/**
	 * 加入数据
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @throws Exception
	 */
	public void add(Integer key, Object value) throws Exception {
		if (this.datas.containsKey(key))
			throw new Exception("key exists");

		this.datas.put(key, value);
	}

	/**
	 * 替换一个数据
	 * 
	 * @param key
	 *            主键
	 * @param value
	 *            值
	 * @throws Exception
	 */
	public void replace(Integer key, Object value) throws Exception {
		if (!this.datas.containsKey(key))
			throw new Exception("key not exists");

		this.datas.replace(key, value);
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 *            主键
	 * @return
	 */
	public Object get(Integer key) {
		return this.datas.get(key);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 *            主键
	 */
	public void remove(Integer key) {
		this.datas.remove(key);
	}

	/**
	 * 将一行数据编码成当前的数据文件的数据 chars转换成字符串
	 * 
	 * @param table
	 *            table对象
	 * @param bytes
	 *            数据
	 * @throws Exception
	 */
	public void code(Table table, byte[] bytes) throws Exception {
		this.datas.clear();

		if (table == null || StringUtils.isEmpty(table.getName()))
			throw new Exception("table not define");

		Field[] fields = table.getFields();

		if (fields == null || fields.length <= 0)
			throw new Exception("there is no fields");

		if (bytes == null || bytes.length != table.getSize())
			throw new Exception("data not match fields define");

		int seek = 0;
		for (Field field : fields) {
			switch (field.getDataType()) {
			case BYTE: {
				this.add(field.getId(), bytes[seek]);
				break;
			}
			case CHAR: {
				this.add(field.getId(), ByteUtil.getChar(bytes, seek));
				break;
			}
			case SHORT: {
				this.add(field.getId(), ByteUtil.getShort(bytes, seek));
				break;
			}
			case INTEGER: {
				this.add(field.getId(), ByteUtil.getInt(bytes, seek));
				break;
			}
			case LONG: {
				this.add(field.getId(), ByteUtil.getLong(bytes, seek));
				break;
			}
			case BOOLEAN: {
				this.add(field.getId(), bytes[seek] == (byte)1);
				break;
			}
			case FLOAT: {
				this.add(field.getId(), ByteUtil.getFloat(bytes, seek));
				break;
			}
			case DOUBLE: {
				this.add(field.getId(), ByteUtil.getDouble(bytes, seek));
				break;
			}
			case CHARS: {
				byte[] chars = new byte[field.getSize()];
				System.arraycopy(bytes, seek, chars, 0, chars.length);
				this.add(field.getId(), new String(chars).trim());
				break;
			}
			default:
				throw new Exception("DataType not define");
			}
			seek += field.getSize();
		}
	}

	/**
	 * 将当前数据对象编码为byte数组
	 * 
	 * @param table
	 *            数据对象
	 * @return 编码好的byte数组
	 * @throws Exception
	 */
	public byte[] decode(Table table) throws Exception {
		if (table == null || StringUtils.isEmpty(table.getName()))
			throw new Exception("table not define");

		Field[] fields = table.getFields();

		if (fields == null || fields.length <= 0)
			throw new Exception("there is no fields");

		if (this.datas == null || this.datas.size() != table.getFields().length)
			throw new Exception("data not match fields define");

		byte[] result = new byte[table.getSize()];
		int seek = 0;

		for (Field field : fields) {
			Object obj = this.datas.get(field.getId());
			switch (field.getDataType()) {
			case BYTE: {
				result[seek] = ((Byte) obj).byteValue();
				break;
			}
			case CHAR: {
				ByteUtil.putChar(result, ((Character) obj).charValue(), seek);
				break;
			}
			case SHORT: {
				ByteUtil.putShort(result, ((Short) obj).shortValue(), seek);
				break;
			}
			case INTEGER: {
				ByteUtil.putInt(result, ((Integer) obj).intValue(), seek);
				break;
			}
			case LONG: {
				ByteUtil.putLong(result, ((Long) obj).longValue(), seek);
				break;
			}
			case BOOLEAN: {
				result[seek] = ((Boolean) obj).booleanValue() ? (byte)1 : (byte)0;
				break;
			}
			case FLOAT: {
				ByteUtil.putFloat(result, ((Float) obj).floatValue(), seek);
				break;
			}
			case DOUBLE: {
				ByteUtil.putDouble(result, ((Double) obj).doubleValue(), seek);
				break;
			}
			case CHARS: {
				byte[] bytes = obj.toString().getBytes(); //字符串对应的数组长度，如果不足，使用默认值
				System.arraycopy(bytes, 0, result, seek, bytes.length);
				break;
			}
			default:
				throw new Exception("DataType not define");
			}
			seek += field.getSize();
		}

		return result;
	}
	
	/**
	 * 将当前数据copy到目的数组对象
	 * @param table 定义的结构
	 * @param target 目标byte数组
	 * @param skip 偏移量
	 * @throws Exception
	 */
	public void copyTo(Table table, byte[] target , int skip) throws Exception {
		if (table == null || StringUtils.isEmpty(table.getName()))
			throw new Exception("table not define");

		Field[] fields = table.getFields();

		if (fields == null || fields.length <= 0)
			throw new Exception("there is no fields");
		
		if(target == null || target.length < skip + table.getSize())
			throw new Exception("data over index");

		if (this.datas == null || this.datas.size() != table.getFields().length)
			throw new Exception("data not match fields define");

		int seek = skip;

		for (Field field : fields) {
			Object obj = this.datas.get(field.getId());
			switch (field.getDataType()) {
			case BYTE: {
				target[seek] = ((Byte) obj).byteValue();
				break;
			}
			case CHAR: {
				ByteUtil.putChar(target, ((Character) obj).charValue(), seek);
				break;
			}
			case SHORT: {
				ByteUtil.putShort(target, ((Short) obj).shortValue(), seek);
				break;
			}
			case INTEGER: {
				ByteUtil.putInt(target, ((Integer) obj).intValue(), seek);
				break;
			}
			case LONG: {
				ByteUtil.putLong(target, ((Long) obj).longValue(), seek);
				break;
			}
			case BOOLEAN: {
				target[seek] = ((Boolean) obj).booleanValue() ? (byte)1 : (byte)0;
				break;
			}
			case FLOAT: {
				ByteUtil.putFloat(target, ((Float) obj).floatValue(), seek);
				break;
			}
			case DOUBLE: {
				ByteUtil.putDouble(target, ((Double) obj).doubleValue(), seek);
				break;
			}
			case CHARS: {
				byte[] bytes = obj.toString().getBytes(); //字符串对应的数组长度，如果不足，使用默认值
				System.arraycopy(bytes, 0, target, seek, bytes.length);
				break;
			}
			default:
				throw new Exception("DataType not define");
			}
			seek += field.getSize();
		}
	}
	
	/**
	 * 从内存段中获取数据
	 * @param table 表定义结构
	 * @param source 原数据
	 * @param skip
	 * @throws Exception
	 */
	public void decodeFrom(Table table, byte[] source , int skip) throws Exception{
		this.datas.clear();

		if (table == null || StringUtils.isEmpty(table.getName()))
			throw new Exception("table not define");

		Field[] fields = table.getFields();

		if (fields == null || fields.length <= 0)
			throw new Exception("there is no fields");

		if (source == null || source.length < skip + table.getSize())
			throw new Exception("not enough for decode");

		int seek = skip;
		for (Field field : fields) {
			switch (field.getDataType()) {
			case BYTE: {
				this.add(field.getId(), source[seek]);
				break;
			}
			case CHAR: {
				this.add(field.getId(), ByteUtil.getChar(source, seek));
				break;
			}
			case SHORT: {
				this.add(field.getId(), ByteUtil.getShort(source, seek));
				break;
			}
			case INTEGER: {
				this.add(field.getId(), ByteUtil.getInt(source, seek));
				break;
			}
			case LONG: {
				this.add(field.getId(), ByteUtil.getLong(source, seek));
				break;
			}
			case BOOLEAN: {
				this.add(field.getId(), source[seek] == (byte)1);
				break;
			}
			case FLOAT: {
				this.add(field.getId(), ByteUtil.getFloat(source, seek));
				break;
			}
			case DOUBLE: {
				this.add(field.getId(), ByteUtil.getDouble(source, seek));
				break;
			}
			case CHARS: {
				byte[] chars = new byte[field.getSize()];
				System.arraycopy(source, seek, chars, 0, chars.length);
				this.add(field.getId(), new String(chars).trim());
				break;
			}
			default:
				throw new Exception("DataType not define");
			}
			seek += field.getSize();
		}
	}

}
