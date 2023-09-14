package com.model2.mvc.web.product;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//@Value("#{commonProperties['pageUnit']}")
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	//@Value("#{commonProperties['pageSize']}")
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	//@RequestMapping("/addProduct.do")
	@RequestMapping( value="addProduct", method=RequestMethod.POST )
	public String addProduct( @ModelAttribute("product") Product product, Model model, MultipartHttpServletRequest request) throws Exception {

		System.out.println("/product/addProduct : POST");
		//
		
		
		String uploadPath = request.getRealPath("/images/uploadFiles/");
	    String fileChangeName = "";
	    String fileMultiName = "";
	    
	    for(int i = 0; i < product.getFiles().length; i++) {
	    	
	        System.out.println("기존 파일명 : " + product.getFiles()[i].getOriginalFilename());
	        
	        fileChangeName = product.getProdName() + "_" + product.getFiles()[i].getOriginalFilename();
	        //==> $ProdName_$FileName.jsp
	        System.out.println("변경된 파일명 : " + fileChangeName);
	        
	        //==> /images/uploadFiles/$Image_File
	        File f = new File(uploadPath + fileChangeName);
	        
	        product.getFiles()[i].transferTo(f);
	        
	        if(i == 0) {
	        	fileMultiName += fileChangeName;
	        }
	        else {
	        	fileMultiName += ","+fileChangeName;
	        }
	    }
	    System.out.println("*"+fileMultiName);
	    
	    product.setFileName(fileMultiName);
	    
	    String[] fileSplitArray = fileMultiName.split(",");
	    //product.setFileName(fileSplitArray);
	    
		System.out.println("ProductController :: addProduct :: " + product);
		productService.addProduct(product);
		
		model.addAttribute("product", product);
		model.addAttribute("fileSplitArray", fileSplitArray);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping( value="getProduct", method=RequestMethod.GET )
	public String getProductGET( @RequestParam("prodNo") int prodNo , Model model, HttpSession session ) throws Exception {
		
		System.out.println("/product/getProduct : POST");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("vo", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	//@RequestMapping("/getProduct.do")
	@RequestMapping( value="getProduct", method=RequestMethod.POST )
	public String getProduct(  @RequestParam(value="menu",required=false) String menu,
								@RequestParam("prodNo") int prodNo , Model model, HttpSession session ) throws Exception {
		
		System.out.println("/product/getProduct : POST");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("vo", product);
		
		return "forward:/product/getProduct.jsp"+"?prodNo="+prodNo;
	}
	
	//@RequestMapping("/listProduct.do")
	@RequestMapping( value="listProduct", method=RequestMethod.GET )
	public String listProductGET( @RequestParam("menu") String menu,
							    @ModelAttribute("search") Search search, 
							   	 Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct : GET");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp?menu=" + menu;
	}
	
	
	//@RequestMapping("/listProduct.do")
	@RequestMapping( value="listProduct", method=RequestMethod.POST )
	public String listProduct( @RequestParam(value="menu",required=false) String menu,
							    @ModelAttribute("search") Search search, 
							   	 Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct : POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp?menu=" + menu;
	}
	
	
	//@RequestMapping("/updateProduct.do")
	@RequestMapping( value="updateProduct", method=RequestMethod.POST )
	public String updateProduct( @ModelAttribute("product") Product product,
								@RequestParam(value="menu",required=false) String menu, Model model ) throws Exception{

		System.out.println("/updateProduct : POST");
		
		//Business Logic
		productService.updateProduct(product);
		product = productService.getProduct(product.getProdNo());
		
		model.addAttribute("vo", product);
		
		System.out.println("/updateProduct" + product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping( value="updateProductView", method=RequestMethod.POST )
	public String updateProductView( @RequestParam("prodNo") int prodNo, 
									 @RequestParam(value="menu",required=false) String menu, Model model ) throws Exception{

		System.out.println("/updateProductView : POST");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		
		// Model 과 View 연결
		
		String fileMultiName = product.getFileName();
		
		String[] fileSplitArray;
		
		
		if(fileMultiName.length() > 0 ) {
			product.setFileSplitArray(fileMultiName.split(","));
		}
		
		model.addAttribute("vo", product);
		
		return "forward:/product/updateProductView.jsp";
	}

}//end of class