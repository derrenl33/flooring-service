/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mt.flooringmastery.controller;

import com.mt.flooringmastery.dao.FlooringMasteryDao;
import com.mt.flooringmastery.dao.FlooringMasteryDaoException;
import com.mt.flooringmastery.dto.Order;
import com.mt.flooringmastery.dto.Product;
import com.mt.flooringmastery.dto.State;
import com.mt.flooringmastery.ui.FlooringMasteryView;
import java.util.List;


/**
 *
 * @author acalvillo
 */
public class FlooringMasteryController {
    
    private FlooringMasteryView view;
    private FlooringMasteryDao dao;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryDao dao) {
        this.view = view;
        this.dao = dao;
    }
    
    
    
    public void run() throws FlooringMasteryDaoException {
        boolean keepGoing = true;
        int menuSelection = 0;
        //try{
            while (keepGoing) {
                //main loop
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrder();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        //System.exit(0);
                        keepGoing = false;
                        break;
                    default:
                        //unknownCommand();
                }
            }
            exitMessage();
        //}catch (){            
        //}
    }
    
    private int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }
    
    
    private void displayOrder(){
        String userDate = view.getValidDate();
        try{
            List<Order> orderList = dao.displayOrders(userDate);
            view.displayOrderCollection(orderList);
        }catch (Exception e){
            view.displayDateError();
        }
    }
    
    private void addOrder() throws FlooringMasteryDaoException{
        String userDate = view.getValidFutureDate();
        String customerName = view.getCustomerName();
        State state = view.getValidState(dao.getStates());
        Product product = view.getValidProduct(dao.getProducts());
        String area = view.getValidArea();
        Order order = dao.orderToAdd(userDate, customerName, state, product, area);
        String yesOrNo = view.displayOrderAndConfirm(order);
        dao.addOrder(userDate, customerName, state, product, area, yesOrNo);
    }
    private void editOrder() throws FlooringMasteryDaoException{
        String userDate = view.getValidDate();
        int orderNumber;
        do{
            orderNumber = view.getOrderNumber();
        }while(!dao.orderExists(userDate, orderNumber));
        String customerName = view.getCustomerName();
        State state = view.getValidState(dao.getStates());
        Product product = view.getValidProduct(dao.getProducts());
        String area = view.getValidArea();
        Order order = dao.orderToEdit(userDate, orderNumber, customerName, state, product, area);
        String yesOrNo = view.displayOrderAndConfirm(order);
        dao.editOrder(userDate, orderNumber, customerName, state, product, area, yesOrNo);
    }
    
    private void removeOrder() throws FlooringMasteryDaoException{
        String userDate = view.getValidDate();
        int orderNumber;
        do{
            orderNumber = view.getOrderNumber();
        }while(!dao.orderExists(userDate, orderNumber));
        Order order = dao.orderToRemove(userDate, orderNumber);
        String yesOrNo = view.displayOrderAndConfirm(order);
        dao.removeOrder(userDate, orderNumber, yesOrNo);
    }
    
   private void unknownCommand(){
        //view.displayUnknownCommandBanner();
   }
   private void exitMessage() {
        view.displayExitBanner();
    }
    
}
