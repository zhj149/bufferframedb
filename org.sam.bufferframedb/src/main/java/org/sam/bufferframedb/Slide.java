package org.sam.bufferframedb;

import java.io.Serializable;

/**
 * 滑动窗口设置对象
 * @author sam
 *
 */
public interface Slide extends Serializable, Cloneable {
	
	/**
	 * 默认缓冲值
	 * 目前暂时设置为1分钟的量，也就是60
	 */
	public static int DEFAULT_BUFFER_SIZE = 60;

	/**
	 * 设置缓冲区大小
	 * @return
	 */
	public int getBufferSize();
	
	/**
	 * 设置缓冲区大小
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize);
	
	/**
	 * 开始异步读取的操作
	 * @throws Exception
	 */
	public void execute() throws Exception;
	
	/**
	 * 关闭异步读取
	 * @throws Exception
	 */
	public void shutdown() throws Exception;
	
	/**
	 * 清空缓存
	 * @throws Exception
	 */
	public void clear() throws Exception;
}
