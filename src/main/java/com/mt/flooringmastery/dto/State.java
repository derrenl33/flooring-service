/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mt.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author acalvillo
 */
public class State {
    private String stateName;
    private String stateFullName;
    private BigDecimal taxRate;

    public State(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }
    public String getStateFullName(){
        return stateFullName;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public void setStateFullName(String stateFullName){
        this.stateFullName = stateFullName;
    }
}
