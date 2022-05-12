/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mt.flooringmastery.ui;


import com.mt.flooringmastery.dto.Order;
import com.mt.flooringmastery.dto.Product;
import com.mt.flooringmastery.dto.State;
import java.util.List;

/**
 *
 * @author acalvillo
 */
public class FlooringMasteryView {
    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection(){
        io.print("***********************************************");
        io.print("Main Menu: ");
        io.print("\t1. Display Orders");
        io.print("\t2. Add an Order");
        io.print("\t3. Edit an Order");
        io.print("\t4. Remove an Order");
        //io.print("\t5. Export All Data");        
        io.print("\t5. Quit"); 
         
        io.print("***********************************************");
        return io.readInt("Please select from the above choices.", 1, 5); 
        
    }
    
    public void displayExitBanner(){
        io.print("Good Bye! (:");
    }
    
    public void displayDateError(){
        io.print("Invalid Date!");
    }
    
    public void displayOrderCollection(List<Order> orderList){
        
        for (Order currentOrder : orderList) {
           io.print("ID: " + currentOrder.getOrderNum());
           io.print("Name: " +currentOrder.getCustomerName());
           io.print("State: " +currentOrder.getState().getStateName());
           io.print("Tax Rate: " +currentOrder.getState().getTaxRate().toString());
           io.print("Product type: " +currentOrder.getProduct().getProductType());
           io.print("Area: " +currentOrder.getArea().toString());
           io.print("Cost/SqFt: " +currentOrder.getProduct().getCostPerSqFt().toString());
           io.print("Labor Cost/SqFt: " +currentOrder.getProduct().getLaborCostPerSqFt().toString());
           io.print("Material Cost: " +currentOrder.getMaterialCost().toString());
           io.print("Labor Cost: " +currentOrder.getLaborCost().toString());
           io.print("Tax: " +currentOrder.getTax().toString());
           io.print("Total: " +currentOrder.getTotal().toString());
           io.print("----------------------------------------------------------------------");
        }         
        io.readEnter("Please hit enter to continue.");
        
        
    }
    
    public String displayOrderAndConfirm(Order order){
        io.print("ID: " + order.getOrderNum());
        io.print("Name: " +order.getCustomerName());
        io.print("State: " +order.getState().getStateName());
        io.print("Tax Rate: " +order.getState().getTaxRate().toString());
        io.print("Product type: " +order.getProduct().getProductType());
        io.print("Area: " +order.getArea().toString());
        io.print("Cost/SqFt: " +order.getProduct().getCostPerSqFt().toString());
        io.print("Labor Cost/SqFt: " +order.getProduct().getLaborCostPerSqFt().toString());
        io.print("Material Cost: " +order.getMaterialCost().toString());
        io.print("Labor Cost: " +order.getLaborCost().toString());
        io.print("Tax: " +order.getTax().toString());
        io.print("Total: " +order.getTotal().toString());
        
        String yesOrNo = io.readString("Are you sure?(Y/N)");
        return yesOrNo;
    }
    
    
    public String getValidDate(){
        String validDate = "";
        validDate = io.readDate("Please enter date in mmddyyy format");//this will read it in correct format
        return validDate;
    }
    
    public int getOrderNumber(){
        return io.readInt("Please enter an order number.");
    }
    
    //for addOrder
    public String getValidFutureDate(){
        String validDate = "";
        validDate = io.readFutureDate("Please enter a future date in mmddyyy format");//this will read it in correct format
        return validDate;
    }
    
    public String getCustomerName(){
        String name = "";
        name = io.readString("Please enter a customer name.");
        while((!name.matches("[a-zA-Z0-9 ,.]+")) && (!name.equals(""))){
            name = io.readString("Please enter valid customer name.");
        }
        return name;
    }
    
    public State getValidState(List<State> stateList){
        String customerState = io.readValidState("Enter your State: ", stateList);
        //create state
        State state = new State(customerState);        
        for(State currentState: stateList){
            if(currentState.getStateName().equals(customerState)){// if they match, add info for that state
                state.setStateFullName(currentState.getStateFullName());
                state.setTaxRate(currentState.getTaxRate());
            }
        }
        return state;
    }
    
    public int displayProductListsAndGetChoice(List<Product> list){
        int count = 1;

        io.print("ProductMenu: ");
        io.print("Product Type" + "\t" + "Cost Per sq ft" + "\t" + "Labor Cost Per sq ft");
        for(Product currentProduct: list){            
            io.print(Integer.toString(count) +": " + currentProduct.getProductType() + "\t\t" + currentProduct.getCostPerSqFt().toString() + "\t\t" + currentProduct.getLaborCostPerSqFt().toString());
            count++;
        }
        int selection = io.readInt("Please select from the above choices.", 1, count-1); 
        return selection;
        //io.print("You choose: " + list.get(selection-1).getProductType() );

        //list.get(selection-1).getProductType();
    }
   
    public Product getValidProduct(List<Product> productList){
        int productType = this.displayProductListsAndGetChoice(productList);
        String productName = productList.get(productType-1).getProductType();
        //create product         
        Product product = new Product(productName);
        product.setCostPerSqFt(productList.get(productType-1).getCostPerSqFt());
        product.setLaborCostPerSqFt(productList.get(productType-1).getLaborCostPerSqFt());
        return product;
    }
    
    public String getValidArea(){
        String customerArea = io.readArea("Enter the Area: ");
        return customerArea;
    }
        
}
