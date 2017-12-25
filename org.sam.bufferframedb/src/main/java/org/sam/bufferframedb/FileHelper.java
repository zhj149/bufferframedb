package org.sam.bufferframedb;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作的接口
 * @author sam
 *
 */
public interface FileHelper<T> {
	
	/**
	 * 删除文件
	 * @return true正确删除
	 * @throws Exception
	 */
	public boolean drop() throws Exception;
	
	/**
	 * 拿到文件操作对象
	 * @return
	 */
	public File getFile();

	/**
	 * 文件地址
	 * @return
	 */
	public String getUrl();
	
	/**
	 * 设置文件地址
	 * @param url
	 */
	public void setUrl(String url);
	
	/**
	 * 链接到文件 读写模式
	 * <br>文件不存在则创建一个新文件                                    
	 * @return true 链接成功 false失败（创建文件不成功）
	 * @throws IOException 抛出异常
	 */
	public boolean readWriteModel() throws IOException;
	
	/**
	 * 创建文件 只写模式
	 * <br>如果文件存在则创建新的
	 * @return true创建成功 false失败（文件只读等）
	 * @throws IOException
	 */
	public boolean writeModel() throws IOException;
	
	/**
	 * 链接到文件 只读模式
	 * @return true 链接成功 false失败（文件不存在）
	 * @throws IOException
	 */
	public boolean readModel() throws IOException;
	
	/**
	 * 移动当前文件操作位置
	 * @param seek 从文件头到目的地址的位置
	 * @throws Exception
	 */
	public void seek(long seek) throws Exception;
	
	/**
	 * 写入数据
	 * @param t 写入数据
	 * @throws Exception
	 */
	public void write(T t) throws Exception;
	
	/**
	 * 读取数据对象
	 * 读取全部
	 * @return
	 * @throws Exception
	 */
	public T read() throws Exception;
	
	/**
	 * 读取一块数据
	 * @param seek 跳过多少
	 * @param length 读取多少
	 * @return
	 * @throws Exception
	 */
	public T read(long seek , int length) throws Exception;
	
	/**
	 * 写入快
	 * @param seek 跳过多少
	 * @param t 写入对象
	 * @return
	 * @throws Exception
	 */
	public void write(long seek , T t) throws Exception;
	
	/**
	 * 直接将文件加入到末尾
	 * 并且文件指针直接调转到末尾
	 * @param t 写入数据
	 * @throws Exception 
	 */
	public void append(T t) throws Exception;
	
	/**
	 * 关闭文件，并把缓冲区写入到文件中
	 * @return true创建成功 false失败（文件只读等）
	 * @throws IOException
	 */
	public boolean close() throws IOException;
	
}
