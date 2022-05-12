/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.flooringmastery;

import com.mt.flooringmastery.controller.FlooringMasteryController;
import com.mt.flooringmastery.dao.FlooringMasteryDao;
import com.mt.flooringmastery.dao.FlooringMasteryDaoException;
import com.mt.flooringmastery.dao.FlooringMasteryDaoImpl;
import com.mt.flooringmastery.ui.FlooringMasteryView;
import com.mt.flooringmastery.ui.UserIO;
import com.mt.flooringmastery.ui.UserIOConsoleImpl;




/**
 *
 * @author Jocoso2218
 */
public class FlooringMastery {   
    
    public static void main(String[] args) throws FlooringMasteryDaoException {
        UserIO myIo = new UserIOConsoleImpl();
        FlooringMasteryView myView = new FlooringMasteryView(myIo);
        FlooringMasteryDao myDao = new FlooringMasteryDaoImpl();
        FlooringMasteryController controller = new FlooringMasteryController(myView, myDao);
        controller.run();
    }
}
