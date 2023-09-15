package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.domain.Purchase;

public class UpdateTranCodeAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

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
			return "redirect:/listPurchase.do";
		}
		
		return "redirect:/listProduct.do?&menu=manage";
	}

}
