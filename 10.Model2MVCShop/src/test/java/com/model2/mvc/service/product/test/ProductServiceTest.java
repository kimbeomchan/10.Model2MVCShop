package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


/*
 *	FileName :  ProductServiceTest.java
 * �� JUnit4 (Test Framework) �� Spring Framework ���� Test( Unit Test)
 * �� Spring �� JUnit 4�� ���� ���� Ŭ������ ���� ������ ��� ���� �׽�Ʈ �ڵ带 �ۼ� �� �� �ִ�.
 * �� @RunWith : Meta-data �� ���� wiring(����,DI) �� ��ü ����ü ����
 * �� @ContextConfiguration : Meta-data location ����
 * �� @Test : �׽�Ʈ ���� �ҽ� ����
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/commonservice.xml" })
public class ProductServiceTest {

	//==>@RunWith,@ContextConfiguration �̿� Wiring, Test �� instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	@Test
	public void testAddProduct() throws Exception {
		Product product = new Product();
		
		product.setProdName("testProdName");
		product.setProdDetail("testProdDetail");
		product.setManuDate("20230808");
		product.setPrice(1004);
		product.setFileName("testFileName");
		
		productService.addProduct(product);
		

		//==> console Ȯ��
		System.out.println(product);
		
		//==> API Ȯ��
//		Assert.assertEquals("10059", product.getProdNo());
//		Assert.assertEquals("testProdName", product.getProdName());
//		Assert.assertEquals("testProdDetail", product.getProdDetail());
//		Assert.assertEquals("2023-08-08", product.getManuDate());
//		Assert.assertEquals(1004, product.getPrice());
//		Assert.assertEquals("testFileName", product.getFileName());
	}
	
	@Test
	public void testGetProduct() throws Exception {
		Product product = new Product();
//		==> �ʿ��ϴٸ�...
//		user.setUserId("testUserId");
//		user.setUserName("testUserName");
//		user.setPassword("testPasswd");
//		user.setSsn("1111112222222");
//		user.setPhone("111-2222-3333");
//		user.setAddr("��⵵");
//		user.setEmail("test@test.com");
		
		product = productService.getProduct(10088);

		//==> console Ȯ��
		//System.out.println(user);
		
		//==> API Ȯ��
//		Assert.assertEquals("testUserId", user.getUserId());
//		Assert.assertEquals("testUserName", user.getUserName());
//		Assert.assertEquals("testPasswd", user.getPassword());
//		Assert.assertEquals("111-2222-3333", user.getPhone());
//		Assert.assertEquals("��⵵", user.getAddr());
//		Assert.assertEquals("test@test.com", user.getEmail());
		//Assert.assertEquals(10088, product.getProdNo());
		Assert.assertEquals("testProdNameUpdate", product.getProdName());
		Assert.assertEquals("testProdDetailUpdate", product.getProdDetail());
		Assert.assertEquals("20230808", product.getManuDate());
		Assert.assertEquals(4001, product.getPrice());
		Assert.assertEquals("testFileNameUpdate", product.getFileName());

		Assert.assertNotNull(productService.getProduct(10001));
		Assert.assertNotNull(productService.getProduct(10002));
	}
	
	@Test
	 public void testUpdateProduct() throws Exception{
		 
		Product product = productService.getProduct(10088);
		Assert.assertNotNull(product);
		
//		Assert.assertEquals("testProdName", product.getProdName());
//		Assert.assertEquals("testProdDetail", product.getProdDetail());
//		Assert.assertEquals(1004, product.getPrice());
//		Assert.assertEquals("testFileName", product.getFileName());

		product.setProdName("testProdNameUpdate");
		product.setProdDetail("testProdDetailUpdate");
		product.setPrice(4001);
		product.setFileName("testFileNameUpdate");
		
		productService.updateProduct(product);
		
		product = productService.getProduct(product.getProdNo());
		Assert.assertNotNull(product);
		
		//==> console Ȯ��
		//System.out.println(user);
			
		//==> API Ȯ��
		Assert.assertEquals("testProdNameUpdate", product.getProdName());
		Assert.assertEquals("testProdDetailUpdate", product.getProdDetail());
		Assert.assertEquals(4001, product.getPrice());
		Assert.assertEquals("testFileNameUpdate", product.getFileName());
	 }
//	 
//	@Test
//	public void testCheckDuplication() throws Exception{
//
//		//==> �ʿ��ϴٸ�...
////		User user = new User();
////		user.setUserId("testUserId");
////		user.setUserName("testUserName");
////		user.setPassword("testPasswd");
////		user.setSsn("1111112222222");
////		user.setPhone("111-2222-3333");
////		user.setAddr("��⵵");
////		user.setEmail("test@test.com");
////		
////		userService.addUser(user);
//		
//		//==> console Ȯ��
//		System.out.println(userService.checkDuplication("testUserId"));
//		System.out.println(userService.checkDuplication("testUserId"+System.currentTimeMillis()) );
//	 	
//		//==> API Ȯ��
//		Assert.assertFalse( userService.checkDuplication("testUserId") );
//	 	Assert.assertTrue( userService.checkDuplication("testUserId"+System.currentTimeMillis()) );
//		 	
//	}
	
	 //==>  �ּ��� Ǯ�� �����ϸ�....
	 @Test
	 public void testGetProductListAll() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console Ȯ��
	 	//System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("");
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	//==> console Ȯ��
	 	//System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 @Test
	 public void testGetProductListByProductNo() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("10088");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
		//==> console Ȯ��
	 	//System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console Ȯ��
	 	//System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 @Test
	 public void testGetProductListByProductName() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("testProdNameUpdate");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console Ȯ��
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console Ȯ��
	 	System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 
	 
	 
	 @Test
	 public void testGetProductListByProductPrice() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("2");
	 	search.setSearchKeyword("4001");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console Ȯ��
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("2");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console Ȯ��
	 	System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 
}