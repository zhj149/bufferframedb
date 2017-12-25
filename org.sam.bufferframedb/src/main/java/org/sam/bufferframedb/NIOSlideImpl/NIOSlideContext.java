package org.sam.bufferframedb.NIOSlideImpl;

import java.util.List;

import org.sam.bufferframedb.AccessModel;
import org.sam.bufferframedb.Context;
import org.sam.bufferframedb.Index;
import org.sam.bufferframedb.Slide;
import org.sam.bufferframedb.Table;

/**
 * NIO实现的具有滑动窗口缓存方式的文件数据库上下文管理对象
 * @author sam
 *
 */
public class NIOSlideContext implements Context , Slide {

	private static final long serialVersionUID = -222690099709663975L;
	
	/**
	 * 关闭的时候是否删除数据库
	 */
	private boolean deleteOnClose = false;
	
	/**
	 * 基础文件
	 */
	private String url = "";

	//begin implements
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDeleteOnClose() {
		return deleteOnClose;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeleteOnClose(boolean delete) {
		this.deleteOnClose = delete;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * 设置
	 * @param url
	 */
	private void setUrl(String url){
		this.url = url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessModel getAccessModel() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccessModel(AccessModel accessModel) throws Exception {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Table getTable() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Index> getIndexs() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getFrame(long frame) throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index append(byte[] data) throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index insert(long before, byte[] data) throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long frame) throws Exception {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(long frame, byte[] data) throws Exception {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void move2Frame(long frame) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {

	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void close(){
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void drop(){
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBufferSize() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBufferSize(int bufferSize) {
		
	}

	@Override
	public byte[] read() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() throws Exception {
		// TODO Auto-generated method stub
		
	}

	//end
}
