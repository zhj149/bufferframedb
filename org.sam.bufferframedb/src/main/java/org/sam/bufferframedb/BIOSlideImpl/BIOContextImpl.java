package org.sam.bufferframedb.BIOSlideImpl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.sam.bufferframedb.AccessModel;
import org.sam.bufferframedb.Context;
import org.sam.bufferframedb.Index;
import org.sam.bufferframedb.Table;
import org.sam.bufferframedb.filehelper.BioFileImpl;
import org.sam.bufferframedb.filehelper.FileHelper;
import org.sam.bufferframedb.filehelper.ObjectFileHelperImpl;

/**
 * BIO实现的文件数据库上下文管理对象 无缓存
 * 
 * @author sam
 *
 */
public class BIOContextImpl implements Context<byte[]> {

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
	 * 当前索引
	 */
	private AtomicLong index = new AtomicLong(0);

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
	public static BIOContextImpl createNew(String url, Table table) throws Exception {

		if (url == null || url.length() <= 0)
			throw new Exception("url not define");

		if (table == null || table.getFields() == null || table.getFields().length <= 0)
			throw new Exception("fields not define");

		BIOContextImpl context = new BIOContextImpl();
		context.setUrl(url);
		String baseName = url;
		if (!url.endsWith("/") && !url.endsWith("\\"))
			baseName += "/";

		baseName = baseName + table.getName();

		context.setTableFileHelper(new ObjectFileHelperImpl(baseName + ".table"));
		context.setIndexFileHelper(new ObjectFileHelperImpl(baseName + ".index"));
		context.setDataFileHelper(new BioFileImpl(baseName + ".data"));

		if (!context.getTableFileHelper().writeModel())
			throw new IOException("create table file fail");

		if (!context.getIndexFileHelper().writeModel())
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
	public static BIOContextImpl connectTo(String url, String table) throws Exception {

		if (url == null || url.length() <= 0)
			throw new Exception("url not define");

		if (table == null || table.length() <= 0)
			throw new Exception("fields not define");

		BIOContextImpl context = new BIOContextImpl();
		context.setUrl(url);
		String baseName = url;
		if (!url.endsWith("/") && !url.endsWith("\\"))
			baseName += "/";

		baseName = baseName + table;

		context.setTableFileHelper(new ObjectFileHelperImpl(baseName + ".table"));
		context.setIndexFileHelper(new ObjectFileHelperImpl(baseName + ".index"));
		context.setDataFileHelper(new BioFileImpl(baseName + ".data"));

		if (!context.getTableFileHelper().readWriteModel())
			throw new IOException("get table file fail");

		if (!context.getIndexFileHelper().readWriteModel())
			throw new IOException("get index file fail");

		if (!context.getDataFileHelper().readWriteModel())
			throw new IOException("get data file fail");

		context.setAccessModel(AccessModel.READWRITE);
		context.setTable((Table) context.tableFileHelper.read());
		context.setIndexs((List<Index>) context.indexFileHelper.read());

		return context;
	}

	/**
	 * 隐藏系统构造函数 <br>
	 * 防止开发人员乱用系统
	 * 
	 */
	private BIOContextImpl() {

	}

	/**
	 * 创建一个索引对象
	 * @param newFrame 新的索引数据
	 * @return
	 */
	private Index createIndex(long newFrame) {

		this.index.incrementAndGet();
		Index result = new Index(newFrame, size.get(), 0);
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
	 * 
	 * @param table
	 */
	private void setTable(Table table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Index> getIndexs() {
		return new LinkedList<>(this.indexs);
	}

	/**
	 * 设置索引列表
	 * 
	 * @param indexs
	 */
	private void setIndexs(List<Index> indexs) {
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
	public byte[] read() throws Exception {
		long f = index.getAndIncrement();
		Index index = this.getIndexs().get((int) f);
		long s = size.getAndAdd(index.getSize());
		byte[] values = this.dataFileHelper.read(s, index.getSize());
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index append(byte[] data) throws Exception {

		long maxFrame = this.getMaxFrame();
		Index index = this.createIndex(maxFrame + 1);

		synchronized (this.indexs) {
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
	public Index append(long frame, byte[] data) throws Exception {

		Index index = this.createIndex(frame);
		
		synchronized (this.indexs) {
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
	public Index insert(long before, long frame, byte[] data) throws Exception {
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
	public void move2Frame(long frame) throws Exception {
		int iFind = this.findNearIndex(frame);
		if (iFind < 0)
			return;
		
		Index index = this.getIndexs().get(iFind);
		this.index.set(iFind);
		this.size.set(index.getBegin());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws Exception {
		try {
			this.tableFileHelper.write(this.table);
			this.indexFileHelper.write(this.indexs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws Exception {
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
	public void drop() throws Exception {
		this.dataFileHelper.close();
		this.tableFileHelper.close();
		this.indexFileHelper.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getFrame() {
		return this.indexs.get((int)this.index.get()).getFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.indexs.size();
	}
	
	/**
	 * 获取第一帧
	 * @return
	 */
	public long getMinFrame(){
		if (this.indexs == null || this.indexs.isEmpty())
			return -1;

		Index index = this.indexs.get(0);
		return index.getFrame();
	}

	/**
	 * 获取最后一帧
	 */
	@Override
	public long getMaxFrame() {
		if (this.indexs == null || this.indexs.isEmpty())
			return -1;

		Index index = this.indexs.get(this.indexs.size() - 1);
		return index.getFrame();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getNextFrame(){
		int current = (int)this.index.get();
		if (this.indexs == null || this.indexs.isEmpty() || current < 0 || current > this.indexs.size() - 1)
			return -1;
		
		return this.indexs.get(current + 1).getFrame();
	}

	// end
}
