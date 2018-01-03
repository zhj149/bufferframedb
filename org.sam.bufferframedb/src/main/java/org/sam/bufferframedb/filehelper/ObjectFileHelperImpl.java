package org.sam.bufferframedb.filehelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 使用流写入的对象
 * 
 * @author sam
 *
 */
public class ObjectFileHelperImpl implements FileHelper<Object> {
	
	/**
	 * 文件的保存地址
	 */
	private String url;
	
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
	 * 文件输入流
	 */
	private FileInputStream input;
	
	/**
	 * 对象输入流
	 */
	private ObjectInputStream ois;
	
	/**
	 * 文件输出流
	 */
	private FileOutputStream output;
	
	/**
	 * 对象输出流
	 */
	private ObjectOutputStream oos;
	
	/**
	 * 文件输入流
	 * @return
	 */
	public FileInputStream getInput() {
		return input;
	}

	/**
	 * 对象输入流
	 * @return
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * 文件输出流
	 * @return
	 */
	public FileOutputStream getOutput() {
		return output;
	}

	/**
	 * 对象输出流
	 * @return
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * 使用流写入的对象
	 * @param url
	 */
	public ObjectFileHelperImpl(String url){
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
		
		input = new FileInputStream(this.file);
		ois = new ObjectInputStream(input);
		
//		output = new FileOutputStream(this.file);
//		oos = new ObjectOutputStream(output);
		
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
		
		output = new FileOutputStream(this.file);
		oos = new ObjectOutputStream(output);
		
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
		
		input = new FileInputStream(this.file);
		ois = new ObjectInputStream(input);
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read() throws Exception {
		return ois.readObject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(long seek, int length) throws Exception {
		ois.skip(seek);
		return ois.readObject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void append(Object t) throws Exception {
		oos.writeObject(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() throws IOException {
		
		if (this.ois != null)
			this.ois.close();
		
		if(this.input != null)
			this.input.close();
		
		if (this.oos != null)
			this.oos.close();
		
		if (this.output != null)
			this.output.close();
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void seek(long seek) throws Exception {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object t) throws Exception {
		oos.writeObject(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(long seek, Object t) throws Exception {
		oos.writeObject(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drop() throws Exception{
		return this.file.delete();
	}
}