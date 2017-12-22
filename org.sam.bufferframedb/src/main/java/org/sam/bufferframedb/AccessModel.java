package org.sam.bufferframedb;

/**
 * 数据库文件访问模式
 * @author sam
 *
 */
public enum AccessModel {

	/**
	 * read-only model
	 */
	READ,
	/**
	 * write-only model
	 */
	WRITE,
	/**
	 * read-write model
	 */
	READWRITE;
}
