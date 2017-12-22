/**
 * 
 */
package org.sam.bufferframedb;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sam.bufferframedb.BIOSlideImpl.BIOSlideContextImpl;

/**
 * @author sam
 *
 */
public class BlockWriteTest {
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

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

		Field field1 = new Field(1, DataTypes.BOOLEAN);
		Field field2 = new Field(2, DataTypes.BYTE);
		Field field3 = new Field(3, DataTypes.CHAR);
		Field field4 = new Field(4, DataTypes.INTEGER);
		Field field5 = new Field(5, DataTypes.SHORT);
		Field field6 = new Field(6, DataTypes.LONG);
		Field field7 = new Field(7, DataTypes.FLOAT);
		Field field8 = new Field(8, DataTypes.DOUBLE);
		Field field9 = new Field(9, DataTypes.CHARS, 50);

		table = new Table("block", field1, field2, field3, field4, field5, field6, field7, field8, field9);

		try {

			testContext = BIOSlideContextImpl.createNew(url, table, 60);

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

			data.add(1, false);
			data.add(2, (byte) 4);
			data.add(3, 'C');
			data.add(4, 25);
			data.add(5, (short) 25);
			data.add(6, 25555L);
			data.add(7, 1.9f);
			data.add(8, 355.21);
			data.add(9, "我有一头小毛驴fdsafdfsdfsdfdfsfffsdfdsfds");
			
			int iSize = table.getSize();
			byte[] block = new byte[iSize * 5];

			for (int i = 0; i < 10000; i++) {
				
				for (int j = 0 ; j < 5; j++){
					data.copyTo(table, block, iSize * j);
				}
				testContext.append(block);
				System.out.println("write:" + (i + 1));
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
		testContext.flush();
		testContext.close();
	}

}