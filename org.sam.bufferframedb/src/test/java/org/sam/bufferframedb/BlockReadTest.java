package org.sam.bufferframedb;

import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sam.bufferframedb.BIOSlideImpl.BIOContextImpl;

public class BlockReadTest {

	/**
	 * 文件数据库上下文对象
	 */
	private Context testContext;

	private Table table;

	@Before
	public void setUp() throws Exception {
		String url = System.getProperty("user.dir");

		try {

			testContext = BIOContextImpl.connectTo(url, "block", 60);
			table = testContext.getTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		testContext.close();
	}

	@Test
	public void test() {
		Data data = new Data();
		try {
			for (Index index : testContext.getIndexs()) {

				System.out.println(index);

				byte[] read = testContext.read();
				int iSize = table.getSize();
				int iLen = read.length / iSize;
				for (int i = 0; i < iLen; i++) {

					data.decodeFrom(table, read, iSize * i);
					
					for (Entry<Integer, Object> entry : data.getDatas().entrySet()) {
						System.out.print("[");
						System.out.print(entry.getKey());
						System.out.print(":");
						System.out.print(entry.getValue());
						System.out.print("]");
					}
				}
				System.out.println("");
				System.out.println("===============================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
