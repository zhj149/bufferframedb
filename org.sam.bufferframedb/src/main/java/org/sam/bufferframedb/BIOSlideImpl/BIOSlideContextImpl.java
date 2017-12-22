package org.sam.bufferframedb.BIOSlideImpl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.sam.bufferframedb.AccessModel;
import org.sam.bufferframedb.Context;
import org.sam.bufferframedb.FileHelper;
import org.sam.bufferframedb.Index;
import org.sam.bufferframedb.Slide;
import org.sam.bufferframedb.Table;

/**
 * NIO实现的具有滑动窗口缓存方式的文件数据库上下文管理对象
 * 
 * @author sam
 *
 */
public class BIOSlideContextImpl implements Context, Slide {

	private static final long serialVersionUID = -222690099709663975L;

	/**
	 * 关闭的时候是否删除数据库
	 */
	private boolean deleteOnClose = false;
	
	/**
	 * 访问类型
	 */
	private AccessModel accessModel = AccessModel.WRITE;
	
	/**
	 * tabel对象
	 */
	private Table table;
	
	/**
	 * 当前的索引文件
	 */
	private List<Index> indexs;

	/**
	 * 表文件对象的助手工具类
	 */
	private FileHelper<Object> tableFileHelper;

	/**
	 * 索引文件助手
	 */
	private FileHelper<Object> indexFileHelper;

	/**
	 * 数据文件助手
	 */
	private FileHelper<byte[]> dataFileHelper;
	
	/**
	 * 当前帧位置
	 */
	private AtomicLong frame = new AtomicLong(0);
	
	/**
	 * 目前的总大小
	 */
	private AtomicLong size = new AtomicLong(0);
	
	/**
	 * 表文件助手
	 * 
	 * @return
	 */
	private FileHelper<Object> getTableFileHelper() {
		return tableFileHelper;
	}

	/**
	 * 表文件助手
	 * 
	 * @param tableFileHelper
	 */
	private void setTableFileHelper(FileHelper<Object> tableFileHelper) {
		this.tableFileHelper = tableFileHelper;
	}

	/**
	 * 索引文件助手
	 * 
	 * @return
	 */
	private FileHelper<Object> getIndexFileHelper() {
		return indexFileHelper;
	}

	/**
	 * 索引文件助手
	 * 
	 * @param indexFileHelper
	 */
	private void setIndexFileHelper(FileHelper<Object> indexFileHelper) {
		this.indexFileHelper = indexFileHelper;
	}

	/**
	 * 数据文件助手
	 * 
	 * @return
	 */
	private FileHelper<byte[]> getDataFileHelper() {
		return dataFileHelper;
	}

	/**
	 * 数据文件助手
	 * 
	 * @param dataFileHelper
	 */
	private void setDataFileHelper(FileHelper<byte[]> dataFileHelper) {
		this.dataFileHelper = dataFileHelper;
	}

	/**
	 * 基础文件
	 */
	private String url = "";

	/**
	 * 创建一个新的库文件 <br>
	 * 如果有旧数据文件则直接覆盖
	 * 
	 * @param url
	 *            基本地址
	 * @param table
	 *            表名和定义
	 * @param buffer
	 *            缓存数量
	 * @return 表操作上下文对象
	 * @throws Exception 
	 */
	public static BIOSlideContextImpl createNew(String url, Table table, int buffer) throws Exception {
		
		if (url == null || url.length() <= 0)
			throw new Exception("url not define");
		
		if (table == null || table.getFields() == null || table.getFields().length <= 0)
			throw new Exception("fields not define");
		
		if (buffer <= 0)
			buffer = Slide.DEFAULT_BUFFER_SIZE;
		
		BIOSlideContextImpl context = new BIOSlideContextImpl();
		context.setUrl(url);
		String baseName = url;
		if (!url.endsWith("/") && !url.endsWith("\\") )
			baseName += "/";
		
		baseName = baseName + table.getName();
			
		context.setTableFileHelper(new ObjectFileHelperImpl(baseName + ".table"));
		context.setIndexFileHelper(new ObjectFileHelperImpl(baseName + ".index"));
		context.setDataFileHelper(new BioFileImpl(baseName + ".data"));
		
		if (!context.getTableFileHelper().writeModel())
			throw new IOException("create table file fail");
		
		if(!context.getIndexFileHelper().writeModel())
			throw new IOException("create index file fail");
		
		if (!context.getDataFileHelper().writeModel())
			throw new IOException("create data file fail");
		
		context.setAccessModel(AccessModel.WRITE);
		context.setTable(table);
		context.setIndexs(new LinkedList<>());
		
		return context;
	}
	
	/**
	 * 链接到数据库 <br>
	 * 如果不存在则创建新的
	 * 
	 * @param url
	 *            基本地址
	 * @param table
	 *            表名 在对象里直接反序列化出表对象
	 * @param buffer
	 *            缓存数量
	 * @return 表操作上下文对象
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static BIOSlideContextImpl connectTo(String url, String table, int buffer) throws Exception {
		
		if (url == null || url.length() <= 0)
			throw new Exception("url not define");
		
		if (table == null || table.length() <= 0)
			throw new Exception("fields not define");
		
		if (buffer <= 0)
			buffer = Slide.DEFAULT_BUFFER_SIZE;
		
		BIOSlideContextImpl context = new BIOSlideContextImpl();
		context.setUrl(url);
		String baseName = url;
		if (!url.endsWith("/") && !url.endsWith("\\") )
			baseName += "/";
		
		baseName = baseName + table;
			
		context.setTableFileHelper(new ObjectFileHelperImpl(baseName + ".table"));
		context.setIndexFileHelper(new ObjectFileHelperImpl(baseName + ".index"));
		context.setDataFileHelper(new BioFileImpl(baseName + ".data"));
		
		if (!context.getTableFileHelper().readWriteModel())
			throw new IOException("get table file fail");
		
		if(!context.getIndexFileHelper().readWriteModel())
			throw new IOException("get index file fail");
		
		if (!context.getDataFileHelper().readWriteModel())
			throw new IOException("get data file fail");
		
		context.setAccessModel(AccessModel.READWRITE);
		context.setTable((Table)context.tableFileHelper.read());
		context.setIndexs((List<Index>)context.indexFileHelper.read());
		
		return context;
	}

	/**
	 * 隐藏系统构造函数 <br>
	 * 防止开发人员乱用系统
	 * 
	 */
	private BIOSlideContextImpl() {
		
	}
	
	/**
	 * 创建一个索引对象
	 * @return
	 */
	private Index createIndex(){
		
		long l = this.frame.incrementAndGet();
		Index result = new Index(l, size.get(), 0);
		return result;
	}

	// begin implements

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
	 * 
	 * @param url
	 */
	private void setUrl(String url) {
		this.url = url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessModel getAccessModel() {
		return this.accessModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccessModel(AccessModel accessModel) throws Exception {
		this.accessModel = accessModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Table getTable() {
		return this.table;
	}
	
	/**
	 * 设置table对象
	 * @param table
	 */
	private void setTable(Table table){
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Index> getIndexs() {
		return this.indexs;
	}
	
	/**
	 * 设置索引列表
	 * @param indexs
	 */
	private void setIndexs(List<Index> indexs){
		this.indexs = indexs;
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
	public byte[] read() throws Exception{
		long f = frame.getAndIncrement();
		Index index = this.getIndexs().get((int)f);
		long s = size.getAndAdd(index.getSize());
		byte[] values = this.dataFileHelper.read(s, index.getSize());
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index append(byte[] data) throws Exception {
		
		Index index = this.createIndex();
		
		synchronized(this.indexs){
			this.size.addAndGet(data.length);
			index.setSize(data.length);
			this.indexs.add(index);
			this.dataFileHelper.append(data);
		}
		
		return index;
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
		try {
			this.tableFileHelper.write(this.table);
			this.indexFileHelper.write(this.indexs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void close(){
		try {
			this.tableFileHelper.close();
			this.indexFileHelper.close();
			this.dataFileHelper.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void drop() {

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

	// end
}
