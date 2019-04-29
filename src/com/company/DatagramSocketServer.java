package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class DatagramSocketServer {
    private DatagramSocket socket;
    Tablero tablero;

    public void init() throws SocketException {
        socket = new DatagramSocket(42069);
        tablero = new Tablero();
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        while(true){

            DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);

            socket.receive(packet);

            sendingData = processData(packet.getData(),packet.getLength());

            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData, sendingData.length, clientIP, clientPort);

            socket.send(packet);
        }
    }
    public byte[] processData(byte[] data, int length) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try{
            ObjectInputStream ois = new ObjectInputStream(in);
            tablero = (Tablero) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tablero.code = 1;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try{
            oos = new ObjectOutputStream(os);
            oos.writeObject(tablero);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocketServer server = new DatagramSocketServer();
        server.init();
        try {
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
