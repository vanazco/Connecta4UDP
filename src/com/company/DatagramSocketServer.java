package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class DatagramSocketServer {
    private DatagramSocket socket;
    private Tablero tablero;
    private boolean checkWinner = false;


    private void init() throws SocketException {
        socket = new DatagramSocket(42069);
    }

    private void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        tablero = new Tablero();

        while(!checkWinner){
            System.out.println(checkWinner);
            checkWinner = !Tablero.getGuanyador().isEmpty();

            DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);

            socket.receive(packet);

            sendingData = processData(packet.getData());

            clientIP = packet.getAddress();

            clientPort = packet.getPort();

            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);

            socket.send(packet);
        }

    }
    private byte[] processData(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try{
            ObjectInputStream ois = new ObjectInputStream(in);
             tablero = (Tablero) ois.readObject();
            if(!Tablero.isTurno()){
                tablero.code = 1;
            } else{
                tablero.code = -1;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    public static void main(String[] args) throws IOException {
        DatagramSocketServer server = new DatagramSocketServer();
        server.init();
        server.runServer();

        DatagramSocketClient socketClient = new DatagramSocketClient();

        String ipsrv;

        ipsrv = "192.168.22.114";

        socketClient.init(ipsrv);
        socketClient.runClient();

    }

}
