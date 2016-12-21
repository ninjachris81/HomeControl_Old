/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import de.hc.commons.sensor.FallbackSensorInput;
import de.hc.commons.sensor.ISensorInputValueChangeListener;
import de.hc.commons.sensor.SensorManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author B1
 */
public class FallbackSensorTest {
    
    public FallbackSensorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void testFallback() throws InterruptedException {
        
        SensorManager sm = new SensorManager();
        FallbackSensorInput si = sm.registerFallbackSensorInput("testValue", 888, 2000);
        
        si.addValueChangeListener(new ISensorInputValueChangeListener() {
            public void onValueChanged(String name, Comparable value) {
                System.out.println("Value " + name + " changed to " + value);
            }
        });
        
        si.setValue(333);
        
        si.setValue(222);

        Thread.sleep(1000);

        si.setValue(112);

        Thread.sleep(1000);

        si.setValue(113);

        Thread.sleep(1000);

        si.setValue(114);

        Thread.sleep(3000);
        
    }
}
