/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.serialcomm;


/**
 *
 * @author B1
 */
public class SimpleAsciiProtocol extends AbstractProtocol {
    
    private String bufferData = "";

    static String DATAGRAM_SEPARATOR = "\r\n";
    boolean isFirstParse = true;
    
    public SimpleAsciiProtocol(DatagramProcessor datagramProcessor) {
        super(datagramProcessor);
    }

    @Override
    public void nextIncomingData(byte[] data) {
        byte id;
        
        //System.out.print(new String(data));
        bufferData+=new String(data);

        if (isFirstParse) {
            // throw away trash
            int i = bufferData.indexOf(DATAGRAM_SEPARATOR);
            if (i >= 0) {
                System.out.println(bufferData);
                bufferData = bufferData.substring(i+2);
                isFirstParse = false;
                System.out.println(bufferData);
            } else {
                return;
            }
        }

        mainloop:
        while (true) {
            int i = bufferData.indexOf(DATAGRAM_SEPARATOR);

            if (i > 2) {
                System.out.print("DATA:::");
                System.out.print(bufferData);
                System.out.print(":::DATA");

                byte dataTypeRaw = Byte.parseByte(bufferData.substring(0,1), 16);
                if (dataTypeRaw < DATA_TYPE.values().length && dataTypeRaw >0) {
                    DATA_TYPE dataType = DATA_TYPE.values()[dataTypeRaw];
                    if (i-2 >= getMinDataSize(dataType)) {
                        id = Byte.parseByte(bufferData.substring(1,2), 16);

                        if (dataType == DATA_TYPE.INT) {
                            datagramProcessor.onNewDatagram(new Datagram<Integer>(id, Integer.parseInt(bufferData.substring(2, i), 16)));
                        } else if (dataType == DATA_TYPE.DOUBLE) {
                            datagramProcessor.onNewDatagram(new Datagram<Double>(id, Double.parseDouble(bufferData.substring(2, i))));
                        } else {
                            System.out.println("Unknown data type: " + dataTypeRaw);
                        }
                    } else {
                        System.out.println("Invalid data size: " + dataType);
                    }
                } else {
                    System.out.println("Unknown data type: " + dataTypeRaw);
                }

                //System.out.println(buffer);
                bufferData = bufferData.substring(i+2);
                
                //System.out.println(bufferData);
            } else {
                break mainloop;
            }
        }
    }
    
    private int getMinDataSize(DATA_TYPE dataType) {
        switch(dataType) {
            case INT:
                return 1;
            case DOUBLE:
                return 17;
            default:
                return -1;
        }
        
    }

    public enum DATA_TYPE {
        UNKNOWN,
        INT,
        DOUBLE
    }

    public class Datagram<TYPE extends Comparable> implements IDatagram {

        public byte id;
        public TYPE value;

        public Datagram(byte id, TYPE value) {
            this.id = id;
            this.value = value;
        }

    }

}
