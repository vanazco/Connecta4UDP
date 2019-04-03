package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DatagramSocketServer {
    DatagramSocket socket;
    Tablero tablero;
    boolean checkWinner = false;


    public void init() throws SocketException {
        socket = new DatagramSocket(42069);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        tablero = new Tablero();

        while(!checkWinner){
            checkWinner = tablero.jugar();
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
        tablero = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try{
            ObjectInputStream ois = new ObjectInputStream(in);
             tablero = (Tablero) ois.readObject();
            System.out.println(tablero.isTurno());
            if(tablero.isTurno()){
                tablero.code = 1;
                tablero.pintarTablero();
            }else{
                tablero.code = -1;
                tablero.pintarTablero();
                tablero.getGuanyador();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try{
            oos = new ObjectOutputStream(os);
            oos.writeObject(tablero);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] respuesta = os.toByteArray();
        return respuesta;
    }

}
