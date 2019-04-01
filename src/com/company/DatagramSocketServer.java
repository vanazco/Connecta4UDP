package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DatagramSocketServer {
    DatagramSocket socket;


    public void init() throws SocketException {
        socket = new DatagramSocket(42069);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;


        while(true){

            DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);

            socket.receive(packet);

            sendingData = processData(packet.getData(), packet.getLength());

            clientIP = packet.getAddress();

            clientPort = packet.getPort();

            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);

            socket.send(packet);

        }

    }
    private byte[] processData(byte[] data, int length) {
        //OBJECTO Tablero
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try{
            ObjectInputStream ois = new ObjectInputStream(in);
            // t = (Tablero) ois.readOnject();
            //System.out.println("Tirada:" + t.jugador +"" + t.posicion);

        } catch (IOException e) {
            e.printStackTrace();
        }//catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        //Comprobar tirada.


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try{
            oos = new ObjectOutputStream(os);
            //oos.writeObject(Tablero);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] respuesta = os.toByteArray();
        return respuesta;

    }

}
