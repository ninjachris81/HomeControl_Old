/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import de.hc.serialcomm.AbstractProtocol;
import de.hc.serialcomm.SerialConnector;
import de.hc.serialcomm.SimpleAsciiProtocol;
import java.io.File;
import java.lang.reflect.Field;
import org.apache.commons.lang.SystemUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author B1
 */
public class SerialCommTest {

    public SerialCommTest() {
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
    public void serialTest() throws Exception {
        AbstractProtocol.DatagramProcessor<SimpleAsciiProtocol.Datagram> datagramProcessor = new AbstractProtocol.DatagramProcessor<SimpleAsciiProtocol.Datagram>() {
            @Override
            public void onNewDatagram(SimpleAsciiProtocol.Datagram datagram) {
                //System.out.println("ID: " + datagram.id);
                //System.out.println("Value: " + datagram.value);
            }
        };

        SimpleAsciiProtocol simpleProtocol;
        simpleProtocol = new SimpleAsciiProtocol(datagramProcessor);

        SerialConnector sc = new SerialConnector(simpleProtocol);

        sc.connect("COM5", 115200, false, true);

        while (true) {
            Thread.sleep(2000);
        }

    }
}
