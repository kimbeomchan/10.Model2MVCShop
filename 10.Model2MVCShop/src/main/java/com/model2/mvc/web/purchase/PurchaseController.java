package com.model2.mvc.web.purchase;

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
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	//@Value("#{commonProperties['pageUnit']}")
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
		
	//@Value("#{commonProperties['pageSize']}")
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	
	@RequestMapping(value="addPurchaseView", method=RequestMethod.POST)
	public ModelAndView addPurchaseView( @ModelAttribute("purchase") Purchase purchase,
											@RequestParam("prodNo") int prodNo, Model model, 
												HttpSession session, ModelAndView modelAndView) throws Exception {

		User user = (User) session.getAttribute("user");
		Product product = productService.getProduct(prodNo);
		
		System.out.println("============== addPurchaseView ::" + purchase + "==============");

		String fileMultiName = product.getFileName();
		
		if(fileMultiName.length() > 0 ) {
			product.setFileSplitArray(fileMultiName.split(","));
		}

		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);

		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase( @ModelAttribute("purchase") Purchase purchase,
										@RequestParam("prodNo") int prodNo, Model model, 
											HttpSession session, ModelAndView modelAndView) throws Exception {

		User user = (User) session.getAttribute("user");
		Product product = productService.getProduct(prodNo);
		product.setProTranCode("1");
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchase.setTranNo(prodNo);
		purchase.setTranCode("1");
		
		System.out.println("============== addPurchase ::" + purchase + "==============");
		purchaseService.insertPurchase(purchase);
		
		//ModelAndView modelandview = new ModelAndView();
		String fileMultiName = product.getFileName();
		
		if(fileMultiName.length() > 0 ) {
			product.setFileSplitArray(fileMultiName.split(","));
		}
		
		modelAndView.addObject("purchase", purchase);
		modelAndView.addObject("product", product);
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="listPurchase", method=RequestMethod.GET)
	public ModelAndView listPurchaseGET( @ModelAttribute("purchase") Purchase purchase,
											Model model,
											@ModelAttribute("search") Search search,
											@RequestParam(value="menu",required=false) String menu,
											HttpSession session, ModelAndView modelAndView) throws Exception {

		System.out.println("============ listPurchase GET ============");
		User user = (User) session.getAttribute("user");
		purchase.setBuyer(user);
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		
		Map<String, Object> map = purchaseService.getPurchaseList(search , user.getUserId());
		
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="listPurchase", method=RequestMethod.POST)
	public ModelAndView listPurchasePOST( @ModelAttribute("purchase") Purchase purchase,
										//@RequestParam("prodNo") int prodNo, Model model,
										@ModelAttribute("search") Search search,
										@RequestParam(value="menu",required=false) String menu,
										HttpSession session, ModelAndView modelAndView) throws Exception {

		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User user = (User) session.getAttribute("user");
		//Product product = productService.getProduct(prodNo);
		
		purchase.setBuyer(user);
		//purchase.setPurchaseProd(product);
		
		
		Map<String, Object> map = purchaseService.getPurchaseList(search , user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
//		String fileMultiName = product.getFileName();
//		
//		if(fileMultiName.length() > 0 ) {
//			product.setFileSplitArray(fileMultiName.split(","));
//		}
		
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.addObject("menu", menu);
		
		modelAndView.addObject("purchase", purchase);
		//modelAndView.addObject("product", product);
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="getPurchase", method=RequestMethod.POST)
	public ModelAndView getPurchasePOST( @ModelAttribute("purchase") Purchase purchase,
											@RequestParam("tranNo") int tranNo,
											Model model, 
											HttpSession session, ModelAndView modelAndView) throws Exception {
		
		User user = (User) session.getAttribute("user");
		//Product product = productService.getProduct(prodNo);
		purchase.setBuyer(user);
		purchase = purchaseService.findPurchase(tranNo);
		
		System.out.println("purchase :: " +purchase);
		//purchase.setPurchaseProd(product);
		
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchaseView", method=RequestMethod.POST)
	public ModelAndView updatePurchaseViewPOST( @ModelAttribute("purchase") Purchase purchase,
												@RequestParam("prodNo") int prodNo,
												@RequestParam("tranNo") int tranNo,
												Model model, 
												HttpSession session, ModelAndView modelAndView) throws Exception {
		
		System.out.println("==================== updatePurchaseView ====================");
		
		
		
//		User user = (User) session.getAttribute("user");
//		purchase.setBuyer(user);
		
		//Product product = productService.getProduct(prodNo);
		//purchase.setPurchaseProd(product);
		
		purchase = purchaseService.findPurchase(tranNo);
		
		System.out.println("purchase :: " +purchase);
		//purchase.setPurchaseProd(product);
		
//		purchase.setTranNo(prodNo);
//		purchase.setTranCode("1");
		
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)
	public ModelAndView updatePurchasePOST( @ModelAttribute("purchase") Purchase purchase,
												//@RequestParam("prodNo") int prodNo,
												@RequestParam("tranNo") int tranNo,
												Model model, 
												HttpSession session, ModelAndView modelAndView) throws Exception {
		
		System.out.println("==================== updatePurchase ====================");
		System.out.println("purchase :: " +purchase);
		
		
//		User user = (User) session.getAttribute("user");
//		purchase.setBuyer(user);
		
		//Product product = productService.getProduct(prodNo);
		//purchase.setPurchaseProd(product);
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.findPurchase(tranNo);
		
		System.out.println("purchase :: " +purchase);
		//purchase.setPurchaseProd(product);
		
//		purchase.setTranNo(prodNo);
//		purchase.setTranCode("1");
		
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/updatePurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCode", method=RequestMethod.POST)
	public ModelAndView updateTranCode( @ModelAttribute("purchase") Purchase purchase,
												//@RequestParam("prodNo") int prodNo,
												@RequestParam("tranNo") int tranNo,
												Model model, 
												HttpSession session, ModelAndView modelAndView) throws Exception {
		
		System.out.println("UpdateTranCodeAction Ω√¿€");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		String tranCode = request.getParameter("tranCode");
		
		ProductService productService = new ProductServiceImpl();
		Product product = productService.getProduct(prodNo);

		Purchase purchase = new Purchase();
		
		purchase.setPurchaseProd(product);
		purchase.getPurchaseProd().setProdNo(prodNo);
		
		purchase.setTranCode(tranCode);
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.updateTranCode(purchase);
		
		if(tranCode.equals("3")) {
			modelAndView.setViewName("redirect:/purchase/listPurchase");
		}
		
		
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/product/listProduct?menu=manage");
		
		return modelAndView;
	}
	
}//end of class
