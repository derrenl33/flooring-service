/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mt.flooringmastery.dao;

import com.mt.flooringmastery.dto.Order;
import com.mt.flooringmastery.dto.Product;
import com.mt.flooringmastery.dto.State;
import java.util.List;

/**
 *
 * @author acalvillo
 */
public interface FlooringMasteryDao {

    List<Order> displayOrders(String orderDate);
    List<State> getStates();
    List<Product> getProducts();
    Order addOrder(String orderDate, String customerName, State state, Product product, String area, String yesOrNo) throws FlooringMasteryDaoException;
    Order orderToAdd(String orderDate, String customerName, State state, Product product, String area) throws FlooringMasteryDaoException;
    Order editOrder(String orderDate, int orderNum, String customerName, State state, Product product, String area, String yesOrNo) throws FlooringMasteryDaoException;
    Order orderToEdit(String orderDate, int orderNum, String customerName, State state, Product product, String area) throws FlooringMasteryDaoException;
    Order removeOrder(String orderDate, int orderNum, String yesOrNo);
    Order orderToRemove(String orderDate, int orderNum);
    public boolean orderExists(String orderDate, int orderNum) throws FlooringMasteryDaoException;
}
