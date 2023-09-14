package com.model2.mvc.service.domain;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Product {
	
	private String fileName;
	private String[] fileSplitArray;
	private MultipartFile[] files;
	private String manuDate;
	private int price;
	private String prodDetail;
	private String prodName;
	private int prodNo;
	private String regDate;
	private String proTranCode;
	
	public Product(){
	}
	
	public String[] getFileSplitArray() {
		return fileSplitArray;
	}
	public void setFileSplitArray(String[] fileSplitArray) {
		this.fileSplitArray = fileSplitArray;
	}
	public String getProTranCode() {
		return proTranCode;
	}
	public void setProTranCode(String proTranCode) {
		this.proTranCode = proTranCode;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getManuDate() {
		return manuDate;
	}
	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getProdDetail() {
		return prodDetail;
	}
	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public int getProdNo() {
		return prodNo;
	}
	public void setProdNo(int prodNo) {
		this.prodNo = prodNo;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

	// Override
	public String toString() {
		return "ProductVO : [fileName]" + fileName
				+ "[manuDate]" + manuDate + "[price]" + price + "[prodDetail]" + prodDetail
				+ "[prodName]" + prodName + "[regDate]" + regDate;
	//	+ "[prodNo]" + prodNo;
	}	
}