/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.serialcomm;

import de.hc.commons.protocol.AbstractProtocol;
import de.hc.commons.connection.Connection;
import de.hc.commons.connection.ConnectionDetails;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
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
public class SerialConnection implements Connection {
    
    private boolean isConnected = false;
    private SerialConnectionDetails details;
    private InputStream in;

    public boolean connect(final ConnectionDetails _details) {
        if (_details instanceof SerialConnectionDetails) {
            details = (SerialConnectionDetails) _details;
            
            try {
                final CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(details.getPortName());
                if (!portIdentifier.isCurrentlyOwned()) {
                    CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                    if (commPort instanceof SerialPort) {
                        SerialPort serialPort = (SerialPort) commPort;
                        serialPort.setSerialPortParams(details.getBaudRate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                        if (details.isCanRead()) {
                            InputStream in = serialPort.getInputStream();
                            (new Thread(new SerialReader(in))).start();
                        }

                        if (details.isCanWrite()) {
                            OutputStream out = serialPort.getOutputStream();
                            InputStream in = details.getProtocol().getInputStream();
                            (new Thread(new SerialWriter(out, in))).start();
                        }

                        isConnected = true;
                        return true;
                    } else {
                        //System.out.println("Error: Only serial ports are handled by this example.");
                    }
                } else {
                    System.out.println("Error: Port is currently in use");
                }
            } catch (PortInUseException ex) {
                Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPortException ex) {
                Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedCommOperationException ex) {
                Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
        }
        
        isConnected = false;
        return false;
    }

    public String getConnectionType() {
        return "SERIAL";
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean disconnect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                    details.getProtocol().nextIncomingData(Arrays.copyOf(tempBuffer, len));
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
        InputStream in;

        public SerialWriter(OutputStream out, InputStream in) {
            this.out = out;
            this.in = in;
        }

        public void run() {
            try {
                int d;
                while ((d = in.read()) != -1) {
                    out.write(d);
                }
            } catch (IOException ex) {
                //TODO make a callback on exception.
            }
        }
    }
    
    private void setupLibPath() {
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
            Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SerialConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
