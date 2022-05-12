/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mt.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author Darren
 */
public class Order {
    private String orderDate;
    private int orderNum;
    
    private String customerName;
    
    private State state;
    
    private Product product;
    
    private BigDecimal area;
    private BigDecimal tax;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal total; 

    public Order(int orderNum) {
        //order ID is the order date and number
        
        this.orderNum = orderNum;
    }

    public String getOrderDate() {
        return orderDate;
    }
    
    public int getOrderNum() {
        return orderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    //State & Tax Rate
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    //productType & costPer & laborCostPer
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    } 
    
    //for calculating total cost
    public BigDecimal getArea() {
        return area;
    }  
    
    public void setArea(BigDecimal area) {
        this.area = area;
    }
    
    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    } 
    
    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    } 
    
    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    } 
    
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
