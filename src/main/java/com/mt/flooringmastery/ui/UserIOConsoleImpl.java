/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mt.flooringmastery.ui;

import com.mt.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Darren
 */
public class UserIOConsoleImpl implements UserIO{

    final private Scanner console = new Scanner(System.in);
    
    
     @Override
    public void print(String msg) {
        System.out.println(msg);//will print msg
    }

    @Override
    public double readDouble(String prompt) {
        boolean invalidInput = true; //asume it's invalid
        double num = 0;
        
        while(invalidInput){//will keep running until a valid double is entered
            
            try{
                String stringValue = this.readString(prompt);
                num = Double.parseDouble(stringValue);//will throw exception unless it's double
                invalidInput = false; //this line will run iff it is an double
            }catch(NumberFormatException e){
                this.print("You did not enter a Double. Please try again.");
            }
        }
        return num;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result;
        do {
            result = this.readDouble(prompt);
        } while (result < min || result > max);

        return result;
        
    }

    @Override
    public float readFloat(String prompt) {
        boolean invalidInput = true; //asume it's invalid
        float num = 0;
        
        while(invalidInput){//will keep running until a valid float is entered
            
            try{
                String stringValue = this.readString(prompt);
                num = Float.parseFloat(stringValue);//will throw exception unless it's float
                invalidInput = false; //this line will run iff it is float
            }catch(NumberFormatException e){
                this.print("You did not enter a Float. Please try again.");
            }
        }
        return num;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result;
        do {
            result = this.readFloat(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public int readInt(String prompt) {
        boolean invalidInput = true; //asume it's invalid
        int num = 0;
        
        while(invalidInput){//will keep running until a valid Int is entered
            
            try{
                String stringValue = this.readString(prompt);
                num = Integer.parseInt(stringValue);//will throw exception unless it's int
                invalidInput = false; //this line will run iff it is an Int
            }catch(NumberFormatException e){
                this.print("You did not enter an Integer. Please try again.");
            }
        }
        return num;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do {
            result = this.readInt(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public long readLong(String prompt) {
        boolean invalidInput = true; //asume it's invalid
        long num = 0;
        
        while(invalidInput){//will keep running until a valid long is entered
            
            try{
                String stringValue = this.readString(prompt);
                num = Long.parseLong(stringValue);//will throw exception unless it's long
                invalidInput = false; //this line will run iff it is an long
            }catch(NumberFormatException e){
                this.print("You did not enter a long. Please try again.");
            }
        }
        return num;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long result;
        do {
            result = this.readLong(prompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public String readString(String prompt) {
        boolean isValid = false;
        String input;
        do {
            System.out.println(prompt);
            input = console.nextLine();
            
            if(input == null || input.isEmpty()){
             System.out.println("You did not enter anything");
            }
            else{
                isValid = true;
            }
            
        }while(!isValid);
        
        return input;// will read string from user
    }

    @Override
    public String readDate(String prompt) {
        boolean isValid = false;
        String input;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Date date = null;
        do {
            
            input = this.readString(prompt);
            
            try {
                
                date = sdf.parse(input);
                isValid = true;
            } catch (ParseException ex) {
                System.out.println("Incorrect format!");
            }
                 
        }while(!isValid);
        
        return input;// will read string from user
    }
    
    @Override
    public String readFutureDate(String prompt) {
        boolean isValid = false;
        String input;
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date date = null;
        do {
            
            input = this.readString(prompt);
            if(input == null || input.isEmpty()){
             System.out.println("You did not enter anything");
            }
            else{
                try {
                    Date currentDate = new Date();
                    date = sdf.parse(input);
                    if(date.after(currentDate)){
                        isValid = true;
                    }
                } catch (ParseException ex) {
                    System.out.println("Incorrect format!");
                }
            }
                 
        }while(!isValid);
        
        return input;// will read string from user
    }
    
    @Override
    public String readValidState(String prompt, List<State> list) {
        String validState = "";   
        boolean isValid = false;
    
        List<String> stateList = new ArrayList<String>();
        for(State currentState: list){
            stateList.add(currentState.getStateName());
        }
        
        /*for(int i = 0; i<list.size(); i++){
            this.print(stateList.get(i));
        }*/
        
        do {
            validState = this.readString(prompt).toUpperCase();
            if(stateList.contains(validState))    {
                isValid = true;
                //this.print("You choose: " + validState);
            }     
            else{
                this.print("Invalid state!");
            }
            
            
        }while(!isValid);
        
        return validState;
    }
    
    @Override
    public String readArea(String prompt) {
        String validArea = "";
        BigDecimal bigDecimal;
        BigDecimal min = new BigDecimal("99");
        
        boolean isValid = false;
 
        do {
            System.out.println(prompt);
            validArea = console.nextLine();
            try{
                bigDecimal = new BigDecimal(validArea);
                
                if(bigDecimal.compareTo(min) == 1){//if input is greater than min
                    isValid = true;
                }
                else{
                    this.print("Area must be greater than 100 sq ft");
                }
                
            }catch(NumberFormatException e){
                this.print("You did not enter an Integer. Please try again.");
            }
            
            
        }while(!isValid);
       
        
        return validArea;
    }

    @Override
    public String readEnter(String prompt) {        
        System.out.println(prompt);
        String input = console.nextLine();
        return input;
    }
    
}
