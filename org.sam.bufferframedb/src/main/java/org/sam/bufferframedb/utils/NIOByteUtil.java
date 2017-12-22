package org.sam.bufferframedb.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class NIOByteUtil {
	
	
	/**
	 * 字符串数组转换成byte数组的方法
	 * 
	 * @param chars
	 * @return
	 */
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	/**
	 * 将字符转换字节的方法
	 * 
	 * @param chars
	 * @return
	 */
	public static byte[] getBytes(char chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(1);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}
}
