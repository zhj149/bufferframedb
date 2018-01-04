package org.sam.bufferframedb;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 数据单表管理对象
 * 
 * @author sam
 *
 */
public interface Context<T> extends Iterator<T> , Serializable {

	/**
	 * 是否关闭时候顺便删除数据库
	 * 
	 * @return
	 */
	public boolean isDeleteOnClose();

	/**
	 * 设置关闭时候顺便删除数据库
	 * 
	 * @param delete
	 */
	public void setDeleteOnClose(boolean delete);

	/**
	 * 获取当前的操作帧
	 * 
	 * @return
	 */
	public long getFrame();
	
	/**
	 * 获取第一帧
	 * @return
	 */
	public long getMinFrame();

	/**
	 * 获取最后一帧
	 * 
	 * @return
	 */
	public long getMaxFrame();
	
	/**
	 *  获取下一个frame的帧号
	 * @return -1没找到
	 */
	public long getNextFrame();

	/**
	 * 获取总帧数
	 * 
	 * @return
	 */
	public int size();

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
	 * 单独获取某一帧的数据 在读模式，读写模式下有效
	 * 
	 * @param frame
	 * @return
	 * @throws Exception
	 */
	public T getFrame(long frame) throws Exception;

	/**
	 * 将数据增加的末尾并返回新的索引对象副本，不影响真实的index数据 <br>
	 * 写，读写模式有效
	 * 
	 * @param data
	 *            要保存的数据
	 * @return 返回新的索引对象副本，不影响真实的index数据
	 * @throws Exception
	 */
	public Index append(T data) throws Exception;

	/**
	 * 将数据增加的末尾并返回新的索引对象副本，不影响真实的index数据 <br>
	 * 写，读写模式有效
	 * 
	 * @param frame
	 *            自己定义的frame，主要是为了和时间序列匹配上
	 * @param data
	 *            数据
	 * @return 返回新的索引对象副本，不影响真实的index数据
	 * @throws Exception
	 */
	public Index append(long frame, T data) throws Exception;

	/**
	 * 插入一行数据，并返回新的索引对象副本，不影响真实的index数据 <br>
	 * 同时被插入之后的frame将重建索引 <br>
	 * 写，读写模式有效
	 * 
	 * @param before
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Index insert(long before, T data) throws Exception;

	/**
	 * 插入一行数据，并返回新的索引对象副本，不影响真实的index数据 <br>
	 * 同时被插入之后的frame将重建索引
	 * 
	 * @param before
	 *            哪帧之前
	 * @param frame
	 *            帧号
	 * @param data
	 *            数据
	 * @return
	 * @throws Exception
	 */
	public Index insert(long before, long frame, T data) throws Exception;

	/**
	 * 移除某一个frame,删除之后的frame将重建索引 <br>
	 * 写，读写模式有效
	 * 
	 * @param frame
	 * @throws Exception
	 */
	public void remove(long frame) throws Exception;

	/**
	 * 更新一个frame数据 <br>
	 * 写，读写模式有效
	 * 
	 * @param frame
	 * @param data
	 * @throws Exception
	 */
	public void update(long frame, T data) throws Exception;

	/**
	 * 根据帧找到对应的index 默认使用2分法
	 * 
	 * @param frame
	 *            帧
	 */
	public default int findIndex(long frame) {

		Index index = null;
		List<Index> indexs = this.getIndexs();
		if (indexs == null || indexs.isEmpty())
			return -1;

		int iBegin = 0, iEnd = indexs.size() - 1;

		while (iBegin <= iEnd) {
			int iMid = (iBegin + iEnd) / 2;
			index = indexs.get(iMid);
			if (index.getFrame() > frame) {
				iEnd = iMid - 1;
			} else if (index.getFrame() < frame) {
				iBegin = iMid + 1;
			} else {
				return iMid;
			}
		}

		return -1;
	}
	
	/**
	 * 查找里自己最近的帧
	 * 向上取
	 * @param frame
	 * @return
	 */
	public default int findNearIndex(long frame){
		int ifind = this.findIndex(frame);
		if (ifind >= 0)
			return ifind;
		
		List<Index> indexs = this.getIndexs();
		if (indexs == null || indexs.isEmpty())
			return -1;
		
		int iBegin = 0, iEnd = indexs.size() - 1;
		Index index = null;
		int iMid = 0;
		
		while (iBegin <= iEnd) {
			iMid = (iBegin + iEnd) / 2;
			index = indexs.get(iMid);
			if (index.getFrame() > frame) {
				iEnd = iMid - 1;
			} else if (index.getFrame() < frame) {
				iBegin = iMid + 1;
			} else {
				return iMid;
			}
		}
		
		//当所有都循环完了，看一下iMid就应该是最接近真实值的地方了
		if(iMid >= 0 && iMid < indexs.size() - 1){
			if (indexs.get(iMid).getFrame() <= frame && indexs.get(iMid + 1).getFrame() >= frame)
				return iMid;
		}

		return -1;
	}

	/**
	 * 切换当前帧的位置 <br>
	 * 读模式，写模式，读写模式有效 <br>
	 * 读模式不改变Index数据 <br>
	 * 写模式将改变从开始frame之后的数据 <br>
	 * 读写模式将继续把数据追加到现在index的结尾，只影响当前读位置的位置
	 * 
	 * @param frame
	 *            帧位置
	 * @throws Exception
	 */
	public void move2Frame(long frame) throws Exception;

	/**
	 * 全部存盘
	 * 
	 * @throws Exception
	 */
	public void flush() throws Exception;

	/**
	 * 关系连接
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	/**
	 * 删除当前数据表
	 * 
	 * @throws Exception
	 */
	public void drop() throws Exception;
}
