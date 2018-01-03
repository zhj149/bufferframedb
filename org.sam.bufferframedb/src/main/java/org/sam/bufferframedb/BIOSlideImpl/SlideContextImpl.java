package org.sam.bufferframedb.BIOSlideImpl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.sam.bufferframedb.AccessModel;
import org.sam.bufferframedb.Context;
import org.sam.bufferframedb.Index;
import org.sam.bufferframedb.Slide;
import org.sam.bufferframedb.Table;

/**
 * BIO实现的，装饰器实现的多线程，带有缓存的文件数据操作上下文对象
 * 
 * @author sam
 *
 */
public class SlideContextImpl implements Context<byte[]>, Slide {

	private static final long serialVersionUID = -7709843134851744374L;

	/**
	 * 被装饰的文件操作上下文对象
	 */
	private Context<byte[]> context;

	/**
	 * 线程池对象
	 */
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * 当前的缓存数据队列
	 */
	private BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

	/**
	 * 被装饰的文件操作上下文对象
	 * 
	 * @return
	 */
	public Context<byte[]> getContext() {
		return context;
	}

	/**
	 * 缓存数量
	 */
	private volatile int bufferSize = Slide.DEFAULT_BUFFER_SIZE;
	
	/**
	 * 被装饰的文件操作上下文对象
	 * 
	 * @param context
	 */
	public void setContext(Context<byte[]> context) {
		this.context = context;
	}

	/**
	 * BIO实现的，装饰器实现的多线程，带有缓存的文件数据操作上下文对象
	 * 
	 * @param context
	 *            数据上下文
	 */
	public SlideContextImpl(Context<byte[]> context) {
		this.setContext(context);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * 开始异步读取的操作
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		this.executor.execute(() -> {
			while (this.context.getFrame() <= this.context.getMaxFrame()) {
				try {
					if (this.queue.size() < this.bufferSize) {
						byte[] read = this.context.read();
						if (read != null)
							this.queue.add(read);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 关闭异步读取
	 * 
	 * @throws Exception
	 */
	public void shutdown() throws Exception {
		this.executor.shutdown();
	}

	/**
	 * 清空缓存
	 * 
	 * @throws Exception
	 */
	public void clear() throws Exception {
		this.queue.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDeleteOnClose() {
		return this.context.isDeleteOnClose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeleteOnClose(boolean delete) {
		this.context.setDeleteOnClose(delete);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		return this.context.getUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessModel getAccessModel() {
		return this.context.getAccessModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccessModel(AccessModel accessModel) throws Exception {
		this.context.setAccessModel(accessModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Table getTable() {
		return this.context.getTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Index> getIndexs() {
		return this.context.getIndexs();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getFrame(long frame) throws Exception {
		return this.context.getFrame(frame);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] read() throws Exception {
		return this.queue.take();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index append(byte[] data) throws Exception {
		return this.context.append(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index insert(long before, byte[] data) throws Exception {
		return this.context.insert(before, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long frame) throws Exception {
		this.context.remove(frame);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(long frame, byte[] data) throws Exception {
		this.context.update(frame, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void move2Frame(long frame) {
		try {
			this.shutdown();
			this.context.move2Frame(frame);
			this.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws Exception {
		this.shutdown();
		this.context.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws Exception {
		this.shutdown();
		this.context.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop() throws Exception {
		this.shutdown();
		this.context.drop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getFrame() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.context.size();
	}

	@Override
	public Index append(long frame, byte[] data) throws Exception {
		return this.context.append(frame, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index insert(long before, long frame, byte[] data) throws Exception {
		return this.context.insert(before, frame, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMaxFrame() {
		return this.context.getMaxFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMinFrame() {
		return this.context.getMinFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getNextFrame() {
		return this.context.getNextFrame();
	}

}
