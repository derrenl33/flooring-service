/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mt.flooringmastery.dao;

import com.mt.flooringmastery.dto.Order;
import com.mt.flooringmastery.dto.Product;
import com.mt.flooringmastery.dto.State;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author acalvillo
 */
public class FlooringMasteryDaoImpl implements FlooringMasteryDao{
    private Map<String, Product> productCollection = new HashMap<>();
    //private Map<Integer, Order> orderCollection = new HashMap<>();
    private Map<String, State> stateCollections = new HashMap<>();
    
    private Map<String, List<Order>> orderCollection = new HashMap<>();
     
    public static final String PRODUCT_FILE = "Data/Products.txt";
    public static final String PRODUCT_DELIMITER = ","; 
    
    public static final String TAXES_FILE = "Data/Taxes.txt";
    public static final String TAXES_DELIMITER = ","; 
    
    public String ORDER_FILE ; //multiple file names...
    public static final String ORDER_DELIMITER = "::"; 
    
    
    @Override
    public List<Order> displayOrders(String orderDate) {
        if (!orderCollection.containsKey(orderDate)){ //if we already have them, no need to 
            try {
                loadOrders(orderDate);
            } catch (FlooringMasteryDaoException ex) {
               
            }
        }
        
    
        return new ArrayList<Order>(orderCollection.get(orderDate));    
    }

    @Override
    public Order addOrder(String orderDate, String customerName, State state, Product product, String area, String yesOrNo) throws FlooringMasteryDaoException{
        if (yesOrNo.equals("Y")){     
            try {
                loadOrdersToAdd(orderDate);
            } catch (FlooringMasteryDaoException ex) {

            }

            int max = 0;
            File folder = new File("Orders");
            File[] listOfFiles = folder.listFiles();
            Scanner myScanner;

            for (File file : listOfFiles) {

                try {
                     // Create Scanner for reading the file
                     myScanner = new Scanner(
                             new BufferedReader(
                                     new FileReader(file)));
                } catch (FileNotFoundException ex) {
                             throw new FlooringMasteryDaoException(
                        "Could not load orders into memory.", ex);
                }

                String currentLine;
                Order currentOrder;
                while (myScanner.hasNextLine()) {
                    currentLine = myScanner.nextLine();
                    currentOrder = unmarshallOrder(currentLine);
                    if (currentOrder.getOrderNum() >= max){
                        max = currentOrder.getOrderNum() + 1;
                    }
                }
                // close scanner
                myScanner.close();
            }



            Order orderToAdd = new Order(max);
            orderToAdd.setCustomerName(customerName); 
            orderToAdd.setState(state);
            orderToAdd.setProduct(product);

            BigDecimal areaBD = new BigDecimal(area);
            orderToAdd.setArea(areaBD);

            //calc costs
            var hundred = new BigDecimal(100);
            orderToAdd.setMaterialCost(areaBD.multiply(orderToAdd.getProduct().getCostPerSqFt()));
            orderToAdd.setLaborCost(areaBD.multiply(orderToAdd.getProduct().getLaborCostPerSqFt()));
            orderToAdd.setTax((orderToAdd.getMaterialCost().add(orderToAdd.getLaborCost())).multiply(orderToAdd.getState().getTaxRate().divide(hundred, 2, RoundingMode.HALF_UP)));
            orderToAdd.setTotal((orderToAdd.getMaterialCost().add(orderToAdd.getLaborCost())).add(orderToAdd.getTax()));       

            List<Order> orderList = orderCollection.get(orderDate);
            orderList.add(orderToAdd);
            orderCollection.put(orderDate, orderList);

            try {
                writeOrder();
            } catch (FlooringMasteryDaoException ex) {

            }
            return orderToAdd;
        }else{
            return null;
        }
    }
    
    @Override
    public Order orderToAdd(String orderDate, String customerName, State state, Product product, String area) throws FlooringMasteryDaoException{
        try {
            loadOrdersToAdd(orderDate);
        } catch (FlooringMasteryDaoException ex) {

        }
        
        int max = 0;
        File folder = new File("Orders");
        File[] listOfFiles = folder.listFiles();
        Scanner myScanner;

        for (File file : listOfFiles) {

            try {
                 // Create Scanner for reading the file
                 myScanner = new Scanner(
                         new BufferedReader(
                                 new FileReader(file)));
            } catch (FileNotFoundException ex) {
                         throw new FlooringMasteryDaoException(
                    "Could not load orders into memory.", ex);
            }

            String currentLine;
            Order currentOrder;
            while (myScanner.hasNextLine()) {
                currentLine = myScanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                if (currentOrder.getOrderNum() >= max){
                    max = currentOrder.getOrderNum() + 1;
                }
            }
            // close scanner
            myScanner.close();
        }
            
        
        
        Order orderToAdd = new Order(max);
        orderToAdd.setCustomerName(customerName); 
        orderToAdd.setState(state);
        orderToAdd.setProduct(product);
        
        BigDecimal areaBD = new BigDecimal(area);
        orderToAdd.setArea(areaBD);
        
        //calc costs
        var hundred = new BigDecimal(100);
        orderToAdd.setMaterialCost(areaBD.multiply(orderToAdd.getProduct().getCostPerSqFt()));
        orderToAdd.setLaborCost(areaBD.multiply(orderToAdd.getProduct().getLaborCostPerSqFt()));
        orderToAdd.setTax((orderToAdd.getMaterialCost().add(orderToAdd.getLaborCost())).multiply(orderToAdd.getState().getTaxRate().divide(hundred, 2, RoundingMode.HALF_UP)));
        orderToAdd.setTotal((orderToAdd.getMaterialCost().add(orderToAdd.getLaborCost())).add(orderToAdd.getTax()));       
                
        return orderToAdd;
    }

    @Override
    public Order editOrder(String orderDate, int orderNum, String customerName, State state, Product product, String area, String yesOrNo) throws FlooringMasteryDaoException {
        loadOrders(orderDate);
        Order orderToEdit = null;
            List<Order> orders = orderCollection.get(orderDate);       

            Iterator<Order> i = orders.iterator();
            while (i.hasNext()) {
                Order o = i.next();
                if(o.getOrderNum() == orderNum){
                    orderToEdit = o;                    
                }
            }
            //prev values in case no edit
            String prevName = orderToEdit.getCustomerName();
            State prevState = orderToEdit.getState();  
            Product prevProduct = orderToEdit.getProduct();
            BigDecimal prevArea = orderToEdit.getArea();
            
            if (yesOrNo.equals("Y")){ 
                orderToEdit.setCustomerName(customerName);
                orderToEdit.setState(state);
                orderToEdit.setProduct(product);

                BigDecimal areaBD = new BigDecimal(area);
                orderToEdit.setArea(areaBD);

                //calc costs
                var hundred = new BigDecimal(100);
                orderToEdit.setMaterialCost(areaBD.multiply(orderToEdit.getProduct().getCostPerSqFt()));
                orderToEdit.setLaborCost(areaBD.multiply(orderToEdit.getProduct().getLaborCostPerSqFt()));
                orderToEdit.setTax((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).multiply(orderToEdit.getState().getTaxRate().divide(hundred, 2, RoundingMode.HALF_UP)));
                orderToEdit.setTotal((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).add(orderToEdit.getTax()));   
            
            writeOrder();
            }else{
                //reset edit
                orderToEdit.setCustomerName(prevName);
                orderToEdit.setState(prevState);
                orderToEdit.setProduct(prevProduct);
                orderToEdit.setArea(prevArea);

                var hundred = new BigDecimal(100);
                orderToEdit.setMaterialCost(orderToEdit.getArea().multiply(orderToEdit.getProduct().getCostPerSqFt()));
                orderToEdit.setLaborCost(orderToEdit.getArea().multiply(orderToEdit.getProduct().getLaborCostPerSqFt()));
                orderToEdit.setTax((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).multiply(orderToEdit.getState().getTaxRate().divide(hundred, 2, RoundingMode.HALF_UP)));
                orderToEdit.setTotal((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).add(orderToEdit.getTax()));
            }
            return orderToEdit;
    }
    
    @Override
    public Order orderToEdit(String orderDate, int orderNum, String customerName, State state, Product product, String area) throws FlooringMasteryDaoException {
        loadOrders(orderDate);
        Order orderToEdit = null;
            List<Order> orders = orderCollection.get(orderDate);       

            Iterator<Order> i = orders.iterator();
            while (i.hasNext()) {
                Order o = i.next();
                if(o.getOrderNum() == orderNum){
                    orderToEdit = o;                    
                }
            }
            orderToEdit.setCustomerName(customerName);
            orderToEdit.setState(state);
            orderToEdit.setProduct(product);
            
            BigDecimal areaBD = new BigDecimal(area);
            orderToEdit.setArea(areaBD);
            
            //calc costs
            var hundred = new BigDecimal(100);
            orderToEdit.setMaterialCost(areaBD.multiply(orderToEdit.getProduct().getCostPerSqFt()));
            orderToEdit.setLaborCost(areaBD.multiply(orderToEdit.getProduct().getLaborCostPerSqFt()));
            orderToEdit.setTax((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).multiply(orderToEdit.getState().getTaxRate().divide(hundred, 2, RoundingMode.HALF_UP)));
            orderToEdit.setTotal((orderToEdit.getMaterialCost().add(orderToEdit.getLaborCost())).add(orderToEdit.getTax()));
            
            return orderToEdit;
    }

    @Override
    public Order removeOrder(String orderDate, int orderNum, String yesOrNo) {
        if (yesOrNo.equals("Y")){    
            try {
                loadOrders(orderDate);
            } catch (FlooringMasteryDaoException ex) {

            }

            Order removedOrder = null;
            List<Order> orders = orderCollection.get(orderDate);       

            Iterator<Order> i = orders.iterator();
            while (i.hasNext()) {
                Order o = i.next();
                if(o.getOrderNum() == orderNum){
                    removedOrder = o;
                    i.remove();
                }
            }        

            try {
                writeOrder();
            } catch (FlooringMasteryDaoException ex) {

            }

            return removedOrder;
        }else{
            return null;
        }
    }
    
    @Override
    public Order orderToRemove(String orderDate, int orderNum){
        try {
            loadOrders(orderDate);
        } catch (FlooringMasteryDaoException ex) {
            
        }
        
        Order foundOrder = null;
        List<Order> orders = orderCollection.get(orderDate);       
        
        Iterator<Order> i = orders.iterator();
        while (i.hasNext()) {
            Order o = i.next();
            if(o.getOrderNum() == orderNum){
                foundOrder = o;
            }
        }
        
        return foundOrder;
    }
    
    @Override
    public boolean orderExists(String orderDate, int orderNum) throws FlooringMasteryDaoException{
        loadOrders(orderDate);
        if(orderCollection.containsKey(orderDate)){
            List<Order> orderList = orderCollection.get(orderDate);
            for(Order currentOrder : orderList){
                if(currentOrder.getOrderNum() == orderNum){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    @Override
    public List<State> getStates() {
        try {
            loadState();
        } catch (FlooringMasteryDaoException ex) {
            
        }
        
        return new ArrayList<State>(stateCollections.values());
    }

    @Override
    public List<Product> getProducts() {
        try {
            loadProduct();
        } catch (FlooringMasteryDaoException ex) {
            
        }
        return new ArrayList<Product>(productCollection.values());  
    }
    /*
    
            PRODUCT FILE STARTS HERE!!
    
    */
    
    
    private Product unmarshallProduct(String productAsText){      
        //ProductType,CostPerSquareFoot,LaborCostPerSquareFoot
        //productTokens[0], productTokens[1], productTokens[2]
        String[] productTokens = productAsText.split(PRODUCT_DELIMITER);
        
        String productType = productTokens[0];
        BigDecimal costBigDecimal = new BigDecimal(productTokens[1]);
        BigDecimal laborBigDecimal = new BigDecimal(productTokens[2]);
        
        Product productFromFile = new Product(productType);
        
        productFromFile.setCostPerSqFt(costBigDecimal);
        productFromFile.setLaborCostPerSqFt(laborBigDecimal);
        
        return productFromFile;
    }
    
    private String marshallProduct(Product aproduct){
        String productAsText = aproduct.getProductType() + PRODUCT_DELIMITER;
        productAsText += aproduct.getCostPerSqFt() + PRODUCT_DELIMITER;
        productAsText += aproduct.getLaborCostPerSqFt().toString();
        
        return productAsText;
    }
    
    private void loadProduct() throws FlooringMasteryDaoException {
        Scanner scanner;
        
        try {
            
            scanner = new Scanner(
                        new BufferedReader(
                                new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryDaoException(
                    "-_- Could not load order data into memory.", e);
        }
        
        String currentLine;        
        Product currentProduct;
        
        while (scanner.hasNextLine()) {
            
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);//create string to object

            productCollection.put(currentProduct.getProductType(), currentProduct); //adds objects to hashmap
        }
        // close scanner
        scanner.close();
        
    }   
    
    /*    
     // *************** This part can be uncommented later if we need to add/modify products to the file. 
    private void writeProduct() throws FlooringMasteryDaoException {
       
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(PRODUCT_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryDaoException(
                    "Could not save student data.", e);
        }

        String productAsText;
        List<Product> productList = this.getProductCollection();
        for (Product currentProduct : productList) {

            productAsText = marshallProduct(currentProduct);
          
            out.println(productAsText);
            
            out.flush();
        }
        out.close();
    }

    */
    
    /*
    
            Taxes FILE STARTS HERE!!
            ***************** needs to be fixed!!!!!!!!!!!**********************
    
    */
    
    
    private State unmarshallState(String stateAsText){      
        //State,         StateName,      TaxRate
        //taxTokens[0], taxTokens[1], taxTokens[2]
        String[] stateTokens = stateAsText.split(TAXES_DELIMITER);
        
        State stateFromFile = new State(stateTokens[0]);
        stateFromFile.setStateFullName(stateTokens[1]);
        BigDecimal stateTaxRate = new BigDecimal(stateTokens[2]);
        stateFromFile.setTaxRate(stateTaxRate);
       
        return stateFromFile;
    }
    
    private String marshallState(State astate){
        String stateAsText = astate.getStateName() + TAXES_DELIMITER;
        stateAsText += astate.getStateFullName();
        stateAsText += astate.getTaxRate();
       
        return stateAsText;
    }
    
    private void loadState() throws FlooringMasteryDaoException {
        Scanner scanner;
        
        try {
            
            scanner = new Scanner(
                        new BufferedReader(
                                new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryDaoException(
                    "-_- Could not load order data into memory.", e);
        }
        
        String currentLine;        
        State currentState;
        
        while (scanner.hasNextLine()) {
            
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);//create string to object

            stateCollections.put(currentState.getStateName(), currentState); //adds objects to hashmap
        }
        // close scanner
        scanner.close();
        
    }  
    
    
    
    /*    
     // *************** This part can be uncommented later if we need to add/modify tax to the file. 
    private void writeTaxes() throws FlooringMasteryDaoException {
       
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(TAXES_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryDaoException(
                    "Could not save student data.", e);
        }

        String taxesAsText;
        List<Taxes> taxesList = this.getTaxesCollection();
        for (Taxes currentTax : taxesList) {

            taxesAsText = marshallTaxes(currentTax);
          
            out.println(taxesAsText);
            
            out.flush();
        }
        out.close();
    }

    */
    
    
    
    
     /*
    
            ORDER FILE STARTS HERE!!
    
    */
    
    //here we pass each line from the opened file
    private Order unmarshallOrder(String orderAsText){      
        //OrderNumber::           CustomerName::                State::             TaxRate::               ProductType::       Area::      
        //orderTokens[0]          orderTokens[1]                orderTokens[2]      orderTokens[3]          orderTokens[4]      orderTokens[5]
        
        //CostPerSquareFoot::     LaborCostPerSquareFoot::      MaterialCost::      LaborCost::             Tax::               Total
        //orderTokens[6]          orderTokens[7]                orderTokens[8]      orderTokens[9]          orderTokens[10]     orderTokens[11]
        
        
        String[] orderTokens = orderAsText.split(ORDER_DELIMITER);
        
        Order orderFromFile = new Order(Integer.valueOf(orderTokens[0])); 
        
        orderFromFile.setCustomerName(orderTokens[1]);
        
        //State
        State stateFromFile = new State(orderTokens[2]);
        BigDecimal taxRate = new BigDecimal(orderTokens[3]).setScale(2, RoundingMode.HALF_UP);        
        stateFromFile.setTaxRate(taxRate);
        orderFromFile.setState(stateFromFile);
        
        //product
        Product productFromFile = new Product(orderTokens[4]);
        BigDecimal costPerSqFt = new BigDecimal(orderTokens[6]).setScale(2, RoundingMode.HALF_UP);
        productFromFile.setCostPerSqFt(costPerSqFt);        
        BigDecimal laborCostPerSqFt = new BigDecimal(orderTokens[7]).setScale(2, RoundingMode.HALF_UP);
        productFromFile.setLaborCostPerSqFt(laborCostPerSqFt);
        orderFromFile.setProduct(productFromFile);
        
        
        //Area
        BigDecimal area = new BigDecimal(orderTokens[5]).setScale(2, RoundingMode.HALF_UP);
        orderFromFile.setArea(area);
        
        //MATERIAL COST
        BigDecimal materialCostBD = new BigDecimal(orderTokens[8]);
        BigDecimal materialCost = materialCostBD.setScale(2, RoundingMode.HALF_UP);
        orderFromFile.setMaterialCost(materialCost);
        
        //LABOR COST
        BigDecimal laborCostBD = new BigDecimal(orderTokens[9]);
        BigDecimal laborCost = laborCostBD.setScale(2, RoundingMode.HALF_UP);
        orderFromFile.setLaborCost(laborCost);
        
        //TAX
        BigDecimal taxBD = new BigDecimal(orderTokens[10]);
        BigDecimal tax = taxBD.setScale(2, RoundingMode.HALF_UP);
        orderFromFile.setTax(tax);
        
        //TOTAL
        BigDecimal totalBD = new BigDecimal(orderTokens[11]);
        BigDecimal total = totalBD.setScale(2, RoundingMode.HALF_UP);
        orderFromFile.setTotal(total);
         
        
        
        return orderFromFile;
    }   
    
        
    private void loadOrders(String orderDate)throws FlooringMasteryDaoException{
        Scanner myScanner;
        
        /*myScanner = new Scanner(System.in);
        System.out.println("Enter date in mmddyyyy format: ");
        String orderDate = myScanner.nextLine();*/

        ORDER_FILE = "Orders/Orders_" + orderDate + ".txt";
        List<Order> newOrderList = new ArrayList<Order>();
        try {
            // Create Scanner for reading the file
            myScanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_FILE)));
        } catch (FileNotFoundException ex) {
            throw new FlooringMasteryDaoException(
                    "Could not load orders into memory.", ex);
        }

        String currentLine;
        Order currentOrder;
        while (myScanner.hasNextLine()) {
            currentLine = myScanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            newOrderList.add(currentOrder);
        }
        orderCollection.put(orderDate, newOrderList);
        // close scanner
        myScanner.close();
    }
    
    private void loadOrdersToAdd(String orderDate)throws FlooringMasteryDaoException{
        Scanner myScanner;
        
        /*myScanner = new Scanner(System.in);
        System.out.println("Enter date in mmddyyyy format: ");
        String orderDate = myScanner.nextLine();*/

        ORDER_FILE = "Orders/Orders_" + orderDate + ".txt";
        File file = new File(ORDER_FILE);
        try {
            file.createNewFile();
        } catch (IOException ex) {
        
        }
        
        List<Order> newOrderList = new ArrayList<Order>();

        try {
             // Create Scanner for reading the file
             myScanner = new Scanner(
                     new BufferedReader(
                             new FileReader(ORDER_FILE)));
        } catch (FileNotFoundException ex) {
                     throw new FlooringMasteryDaoException(
                "Could not load orders into memory.", ex);
        }


        String currentLine;
        Order currentOrder;
        while (myScanner.hasNextLine()) {
            currentLine = myScanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            newOrderList.add(currentOrder);
        }
        orderCollection.put(orderDate, newOrderList);
        // close scanner
        myScanner.close();
    }

    
    private String marshallOrder(Order newOrder){
        //OrderNumber::           CustomerName::                State::             TaxRate::               ProductType::       Area::      
        //orderTokens[0]          orderTokens[1]                orderTokens[2]      orderTokens[3]          orderTokens[4]      orderTokens[5]
        
        //CostPerSquareFoot::     LaborCostPerSquareFoot::      MaterialCost::      LaborCost::             Tax::               Total
        //orderTokens[6]          orderTokens[7]                orderTokens[8]      orderTokens[9]          orderTokens[10]     orderTokens[11]
        
        String orderAsText = Integer.toString(newOrder.getOrderNum()) + ORDER_DELIMITER; //0
        orderAsText += newOrder.getCustomerName() + ORDER_DELIMITER; //1
        orderAsText += newOrder.getState().getStateName() + ORDER_DELIMITER; //2
        orderAsText += newOrder.getState().getTaxRate().toString() + ORDER_DELIMITER; //3
        orderAsText += newOrder.getProduct().getProductType() + ORDER_DELIMITER; //4
        orderAsText += newOrder.getArea().toString() + ORDER_DELIMITER; //5
        orderAsText += newOrder.getProduct().getCostPerSqFt().toString() + ORDER_DELIMITER; //6
        orderAsText += newOrder.getProduct().getLaborCostPerSqFt().toString() + ORDER_DELIMITER; //7
        orderAsText += newOrder.getMaterialCost().toString() + ORDER_DELIMITER; //8
        orderAsText += newOrder.getLaborCost().toString() + ORDER_DELIMITER; //9 
        orderAsText += newOrder.getTax().toString() + ORDER_DELIMITER; //10
        orderAsText += newOrder.getTotal().toString() ; //11
        
        
        
        return orderAsText;
    }
    
    /*private void writeOrder() throws FlooringMasteryDaoException{
        
        String orderAsText;
        List<Order> orderList = this.displayOrders();        

        
        for (Order currentOrder : orderList) {
            
            PrintWriter out;
            ORDER_FILE = "Orders/Orders_" + currentOrder.getOrderDate() + ".txt";
            
            try {
                out = new PrintWriter(new FileWriter(ORDER_FILE));
            } catch (IOException e) {
                throw new FlooringMasteryDaoException(
                        "Could not save order data.", e);
            }
            orderAsText = marshallOrder(currentOrder);
          
            out.println(orderAsText);
            
            out.flush();
            out.close();
        }

    }*/
    
        private void writeOrder() throws FlooringMasteryDaoException{
        
            
            PrintWriter out;
            
            try {
                out = new PrintWriter(new FileWriter(ORDER_FILE));
            } catch (IOException e) {
                throw new FlooringMasteryDaoException(
                        "Could not save order data.", e);
            }
            
            String orderAsText; 
            String unparsedDate = ORDER_FILE;
            String parsedDate = unparsedDate.replace("Orders/Orders_", "");
            parsedDate = parsedDate.replace(".txt", "");
            //System.out.println(parsedDate);
            List<Order> orderList = this.displayOrders(parsedDate);  
            for (Order currentOrder : orderList) {
                orderAsText = marshallOrder(currentOrder);         
                out.println(orderAsText);           
                out.flush();
            }
            out.close();
        }

}

