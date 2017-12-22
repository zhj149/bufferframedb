package org.sam.bufferframedb.utils;

/**
 * byte到类型的转换工具
 * 
 * @author sam
 *
 */
public class ByteUtil {
	/**
	 * 转换short为byte
	 * 
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 */
	public static void putShort(byte bb[], short s, int index) {
		bb[index + 1] = (byte) (s >> 8);
		bb[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 通过byte数组取到short
	 * 
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShort(byte[] bb, int index) {
		return (short) (((bb[index + 1] << 8) | bb[index + 0] & 0xff));
	}

	/**
	 * 转换int为byte数组
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putInt(byte[] bb, int x, int index) {
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	/**
	 * 通过byte数组取到int
	 * 
	 * @param bb
	 * @param index
	 *            第几位开始
	 * @return
	 */
	public static int getInt(byte[] bb, int index) {
		return (int) ((((bb[index + 3] & 0xff) << 24) | ((bb[index + 2] & 0xff) << 16) | ((bb[index + 1] & 0xff) << 8)
				| ((bb[index + 0] & 0xff) << 0)));
	}

	/**
	 * 转换long型为byte数组
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putLong(byte[] bb, long x, int index) {
		bb[index + 7] = (byte) (x >> 56);
		bb[index + 6] = (byte) (x >> 48);
		bb[index + 5] = (byte) (x >> 40);
		bb[index + 4] = (byte) (x >> 32);
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	/**
	 * 通过byte数组取到long
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 7] & 0xff) << 56) | (((long) bb[index + 6] & 0xff) << 48)
				| (((long) bb[index + 5] & 0xff) << 40) | (((long) bb[index + 4] & 0xff) << 32)
				| (((long) bb[index + 3] & 0xff) << 24) | (((long) bb[index + 2] & 0xff) << 16)
				| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
	}

	/**
	 * 字符到字节转换
	 * 
	 * @param ch
	 * @return
	 */
	public static void putChar(byte[] bb, char ch, int index) {
		short s = (short) ch;
		bb[index + 1] = (byte) (s >> 8);
		bb[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 字节到字符转换
	 * 
	 * @param b
	 * @return
	 */
	public static char getChar(byte[] bb, int index) {
		return (char) (((bb[index + 1] << 8) | bb[index + 0] & 0xff));
	}

	/**
	 * float转换byte
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putFloat(byte[] bb, float x, int index) {
		int l = Float.floatToIntBits(x);
		bb[index + 3] = (byte) (l >> 24);
		bb[index + 2] = (byte) (l >> 16);
		bb[index + 1] = (byte) (l >> 8);
		bb[index + 0] = (byte) (l >> 0);
	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static float getFloat(byte[] bb, int index) {
		int l = (int) ((bb[index + 3] & 0xff) << 24) | ((bb[index + 2] & 0xff) << 16) | ((bb[index + 1] & 0xff) << 8)
				| ((bb[index + 0] & 0xff) << 0);
		return Float.intBitsToFloat(l);
	}

	/**
	 * double转换byte
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putDouble(byte[] bb, double x, int index) {
		long l = Double.doubleToLongBits(x);
		bb[index + 7] = (byte) (l >> 56);
		bb[index + 6] = (byte) (l >> 48);
		bb[index + 5] = (byte) (l >> 40);
		bb[index + 4] = (byte) (l >> 32);
		bb[index + 3] = (byte) (l >> 24);
		bb[index + 2] = (byte) (l >> 16);
		bb[index + 1] = (byte) (l >> 8);
		bb[index + 0] = (byte) (l >> 0);
	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static double getDouble(byte[] bb, int index) {
		long l = (((long) bb[index + 7] & 0xff) << 56) | (((long) bb[index + 6] & 0xff) << 48)
		| (((long) bb[index + 5] & 0xff) << 40) | (((long) bb[index + 4] & 0xff) << 32)
		| (((long) bb[index + 3] & 0xff) << 24) | (((long) bb[index + 2] & 0xff) << 16)
		| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0);
		return Double.longBitsToDouble(l);
	}
}
