package org.sam.bufferframedb;

import java.io.Serializable;

/**
 * 索引对象
 * @author sam
 *
 */
public class Index implements Serializable , Cloneable {

	private static final long serialVersionUID = -2096398088216842475L;

	/**
	 * 当前frame起始行位置
	 */
	private long begin;
	
	/**
	 * 当前frame的总数据量
	 */
	private int size;
	
	/**
	 * 当前的帧号
	 * 从1开始
	 */
	private long frame;

	/**
	 * 当前frame起始行位置
	 * @return
	 */
	public long getBegin() {
		return begin;
	}

	/**
	 * 当前frame起始行位置
	 * @param begin
	 */
	public void setBegin(long begin) {
		this.begin = begin;
	}

	/**
	 * 当前frame的总数据量
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 当前frame的总数据量
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 当前的帧号
	 * 从1开始
	 * @return
	 */
	public long getFrame() {
		return frame;
	}

	/**
	 * 当前的帧号
	 * 从1开始
	 * @param frame
	 */
	public void setFrame(long frame) {
		this.frame = frame;
	}
	
	/**
	 * 创建一个新的索引对象
	 * @param frame 当前帧号
	 * @param begin 开始位置
	 * @param size 总数大小
	 */
	public Index(long frame , long begin , int size){
		this.setFrame(frame);
		this.setBegin(begin);
		this.setSize(size);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Index clone() throws CloneNotSupportedException {
		return (Index)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "{frame:" + this.frame + ",beginIndex:" + this.begin + ",size:" + this.size + "}";
	}
	
}
