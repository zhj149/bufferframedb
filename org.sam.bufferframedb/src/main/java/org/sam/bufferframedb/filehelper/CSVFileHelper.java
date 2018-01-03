package org.sam.bufferframedb.filehelper;

import java.io.File;
import java.io.IOException;

/**
 *  csv行模式的文件读写实现
 * @author sam
 *
 */
public class CSVFileHelper implements FileHelper<String> {

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean drop() throws Exception {
		return false;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return null;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		return null;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setUrl(String url) {
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean readWriteModel() throws IOException {
		return false;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean writeModel() throws IOException {
		return false;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean readModel() throws IOException {
		return false;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void seek(long seek) throws Exception {
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void write(String t) throws Exception {
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String read() throws Exception {
		return null;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String read(long seek, int length) throws Exception {
		return null;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void write(long seek, String t) throws Exception {
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void append(String t) throws Exception {
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean close() throws IOException {
		return false;
	}

}
