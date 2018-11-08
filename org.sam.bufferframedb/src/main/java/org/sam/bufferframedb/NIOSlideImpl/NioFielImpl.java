package org.sam.bufferframedb.NIOSlideImpl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.sam.bufferframedb.filehelper.FileHelper;

/**
 * 用NIO实现的文件存储实现
 * 
 * @author sam
 *
 */
public class NioFielImpl implements FileHelper<byte[]> {

	/**
	 * 当前操作的文件对象
	 */
	protected File file;

	/**
	 * 文件随机访问对象工具
	 */
	protected RandomAccessFile randomFile;

	/**
	 * 文件channel
	 */
	protected FileChannel channel;

	/**
	 * 文件的保存地址
	 */
	private String url;

	/**
	 * 文件随机访问对象工具
	 * 
	 * @return
	 */
	public RandomAccessFile getRandomFile() {
		return randomFile;
	}

	/**
	 * 
	 * @param url
	 */
	public NioFielImpl(String url) {
		this.setUrl(url);
	}

	/**
	 * 删除操作
	 */
	@Override
	public boolean drop() throws Exception {

		if (this.channel != null && this.channel.isOpen())
			this.channel.close();

		if (this.randomFile != null)
			this.randomFile.close();

		return this.file.delete();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		return this.url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean readWriteModel() throws IOException {
		this.file = new File(this.url);
		if (!this.file.exists()) {
			if (!this.file.createNewFile())
				return false;
		}

		// 只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "rw");
		channel = randomFile.getChannel();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean writeModel() throws IOException {
		this.file = new File(this.url);
		this.file.delete();

		if (!this.file.createNewFile())
			return false;

		// 只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "rw");
		channel = randomFile.getChannel();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean readModel() throws IOException {
		this.file = new File(this.url);
		if (!this.file.exists())
			return false;

		// 只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "r");
		channel = randomFile.getChannel();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void seek(long seek) throws Exception {
		randomFile.seek(seek);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] t) throws Exception {
		if (t == null || t.length <= 0)
			return;

		ByteBuffer buffer = ByteBuffer.allocate(t.length);
		this.channel.write(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] read() throws Exception {

		byte[] result = new byte[(int) this.file.length()];
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
		int bytesRead = channel.read(buffer);
		int index = 0;

		while (bytesRead != -1) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				result[index] = buffer.get();
				index++;
			}
			buffer.clear();
			bytesRead = channel.read(buffer);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] read(long seek, int length) throws Exception {
		
		int block = 1024 * 1024;
		this.randomFile.seek(seek);
		byte[] result = new byte[length];
		
		int count = (int) Math.ceil(length / block);
		ByteBuffer[] buffers = new ByteBuffer[count];
		for (int i = 0; i < count; i++) {
			if (i == count - 1) {
				buffers[i] = ByteBuffer.allocate(length - block * i);
			} else {
				buffers[i] = ByteBuffer.allocate(block);
			}
		}

		long read = this.channel.read(buffers, 0, length);
		int index = 0;
		if (read >= 0){
			for(ByteBuffer buffer : buffers){
				buffer.flip();
				while (buffer.hasRemaining()) {
					result[index] = buffer.get();
					index++;
				}
				buffer.clear();
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(long seek, byte[] t) throws Exception {
		if (t == null || t.length <= 0)
			return;
		
		this.randomFile.seek(seek);
		ByteBuffer buffer = ByteBuffer.wrap(t);
		this.channel.write(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void append(byte[] t) throws Exception {
		if (t == null || t.length <= 0)
			return;
		
		this.randomFile.seek(randomFile.length() - 1);
		ByteBuffer buffer = ByteBuffer.wrap(t);
		this.channel.write(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() throws IOException {
		channel.close();
		randomFile.close();
		return false;
	}
}
