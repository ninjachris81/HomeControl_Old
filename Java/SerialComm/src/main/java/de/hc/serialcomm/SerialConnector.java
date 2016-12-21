/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.serialcomm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;

/**
 *
 * @author B1
 */
public class SerialConnector {
    
    private final AbstractProtocol myProtocol;

    public SerialConnector(AbstractProtocol myProtocol) {
        this.myProtocol = myProtocol;

        try {
            String libPath;
            if (SystemUtils.IS_OS_WINDOWS) {
                libPath = new File("").toPath().resolve("lib/Windows/i368-mingw32").toAbsolutePath().toString();
            } else if (SystemUtils.IS_OS_LINUX) {
                libPath = new File("").toPath().resolve("lib/Linux/x86_64-unknown-linux-gnu").toAbsolutePath().toString();
            } else {
                System.out.println("OS not supported");
                return;
            }
            
            System.out.println("Adding library path " + libPath);
            
            System.setProperty("java.library.path", libPath + ";" + System.getProperty("java.library.path"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            
            System.out.println(System.getProperty("java.library.path"));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect(String portName, int baudRate, boolean canWrite, boolean canRead) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                if (canRead) {
                    InputStream in = serialPort.getInputStream();
                    (new Thread(new SerialReader(in))).start();
                }
                
                if (canWrite) {
                    OutputStream out = serialPort.getOutputStream();
                    (new Thread(new SerialWriter(out))).start();
                }

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     *      */
    public class SerialReader implements Runnable {

        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] tempBuffer = new byte[512];
            
            int len = -1;
            try {
                while ((len = this.in.read(tempBuffer)) > -1) {
                    myProtocol.nextIncomingData(Arrays.copyOf(tempBuffer, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *      */
    public class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
