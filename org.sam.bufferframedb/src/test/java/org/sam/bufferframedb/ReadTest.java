package org.sam.bufferframedb;

import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sam.bufferframedb.BIOSlideImpl.BIOContextImpl;

public class ReadTest {

	/**
	 * 文件数据库上下文对象
	 */
	private Context testContext;

	private Table table;

	/**
	 * 初始化
	 */
	@Before
	public void init() {
		String url = System.getProperty("user.dir");

		try {

			testContext = BIOContextImpl.connectTo(url, "test", 60);
			table = testContext.getTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试环节
	 */
	@Test
	public void test() {
		Data data = new Data();
		try {
			for (Index index : testContext.getIndexs()) {
				System.out.println(index);
				byte[] read = testContext.read();
				data.code(table, read);
				for (Entry<Integer, Object> entry : data.getDatas().entrySet()) {
					System.out.print("[");
					System.out.print(entry.getKey());
					System.out.print(":");
					System.out.print(entry.getValue());
					System.out.print("]");
				}
				System.out.println("");
				System.out.println("===============================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放资源
	 */
	@After
	public void destroy() {
		try {
			testContext.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
