package org.sam.bufferframedb;

import java.io.Serializable;
import java.util.List;

/**
 * 数据单表管理对象
 * 
 * @author sam
 *
 */
public interface Context extends Serializable {
	
	/**
	 * 是否关闭时候顺便删除数据库
	 * @return
	 */
	public boolean isDeleteOnClose();

	/**
	 * 设置关闭时候顺便删除数据库
	 * @param delete
	 */
	public void setDeleteOnClose(boolean delete);
	
	/**
	 * 获取访问地址 基础地址，不包含表名
	 * 
	 * @return
	 */
	public String getUrl();

	/**
	 * 获取当前的访问模式
	 * 
	 * @return
	 */
	public AccessModel getAccessModel();

	/**
	 * 切换当前的访问模式 <br>
	 * 其实这么设计不太符合单一职责原则
	 * 
	 * @param accessModel
	 * @throws Exception
	 */
	public void setAccessModel(AccessModel accessModel) throws Exception;

	/**
	 * 获取table对象定义
	 * 
	 * @return
	 */
	public Table getTable();

	/**
	 * 返回当前的数据索引列表的副本
	 * 
	 * @return
	 */
	public List<Index> getIndexs();
	
	/**
	 * 单独获取某一帧的数据
	 * 在读模式，读写模式下有效
	 * @param frame
	 * @return
	 * @throws Exception
	 */
	public byte[] getFrame(long frame) throws Exception;
	
	/**
	 * 读取当前帧，并移动到下一行
	 * @return
	 * @throws Exception
	 */
	public byte[] read() throws Exception;
	
	/**
	 * 将数据增加的末尾并返回新的索引对象副本，不影响真实的index数据
	 * <br>写，读写模式有效
	 * @param data 要保存的数据
	 * @return 返回新的索引对象副本，不影响真实的index数据
	 * @throws Exception
	 */
	public Index append(byte[] data) throws Exception;

	/**
	 * 插入一行数据，并返回新的索引对象副本，不影响真实的index数据
	 * <br>同时被插入之后的frame将重建索引
	 * <br>写，读写模式有效
	 * @param before
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Index insert(long before , byte[] data) throws Exception;
	
	/**
	 * 移除某一个frame,删除之后的frame将重建索引
	 * <br>写，读写模式有效
	 * @param frame
	 * @throws Exception
	 */
	public void remove(long frame) throws Exception;
	
	/**
	 * 更新一个frame数据
	 * <br>写，读写模式有效
	 * @param frame
	 * @param data
	 * @throws Exception
	 */
	public void update(long frame ,byte[] data) throws Exception;

	/**
	 * 切换当前帧的位置
	 * <br>读模式，写模式，读写模式有效
	 * <br>读模式不改变Index数据
	 * <br>写模式将改变从开始frame之后的数据
	 * <br>读写模式将继续把数据追加到现在index的结尾，只影响当前读位置的位置
	 * @param frame 帧位置
	 */
	public void move2Frame(long frame);
	
	/**
	 * 全部存盘
	 */
	public void flush();
	
	/**
	 * 关系连接
	 */
	public void close();
	
	/**
	 * 删除当前数据表
	 */
	public void drop();
}
