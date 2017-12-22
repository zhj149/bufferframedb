package org.sam.bufferframedb.BIOSlideImpl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.sam.bufferframedb.FileHelper;

/**
 * bio的file文件操作对象
 * @author sam
 *
 */
public class BioFileImpl implements FileHelper<byte[]> {
	
	/**
	 * 文件的保存地址
	 */
	private String url;

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
	 * 当前操作的文件对象
	 */
	private File file;
	
	/**
	 * 当前操作的文件对象
	 * @return
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * 文件随机访问对象工具
	 */
	private RandomAccessFile randomFile;
	
	/**
	 * 文件随机访问对象工具
	 * @return
	 */
	public RandomAccessFile getRandomFile() {
		return randomFile;
	}

	/**
	 * 创建文件的地址
	 * @param url
	 */
	public BioFileImpl(String url){
		this.setUrl(url);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean readWriteModel() throws IOException {
		this.file = new File(this.url);
		if (!this.file.exists()){
			if (!this.file.createNewFile())
				return false;
		}
		
		//只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "rw");
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
		
		//只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "rw");
		
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
		
		//只读模式文件访问工具
		randomFile = new RandomAccessFile(this.file, "r");
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void seek(long seek) throws Exception{
		randomFile.seek(seek);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void append(byte[] t) throws Exception{
		randomFile.seek(randomFile.length());
		randomFile.write(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() throws IOException {
		randomFile.close();
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] t) throws Exception {
		randomFile.write(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] read() throws Exception {
		long length = randomFile.length();
		byte[] bytes = new byte[(int)length];
		randomFile.seek(0);
		randomFile.read(bytes);
		return bytes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] read(long seek, int length) throws Exception {
		byte[] block = new byte[(int)length];
		randomFile.seek(seek);
		randomFile.read(block);
		return block;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(long seek, byte[] t) throws Exception {
		randomFile.seek(seek);
		randomFile.write(t);
	}

}
