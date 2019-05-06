package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class DatagramSocketServer {
    private DatagramSocket socket;
    Tablero tablero;
    boolean terminar = false;

    public void init() throws SocketException {
        socket = new DatagramSocket(42069);

    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        tablero = new Tablero();

        while(!terminar){

            DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);

            socket.receive(packet);

            sendingData = processData(packet.getData());


            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData, sendingData.length, clientIP, clientPort);

            socket.send(packet);

            //Verificar si cal acabar
            if(tablero.getGuanyador() != ""){
                terminar = true;
            }
        }
    }
    public byte[] processData(byte[] data) {
        Tirada tirada = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            tirada = (Tirada) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tablero.tirar(tirada.columna);
        tablero.jugar();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(tablero);
            oos.flush();
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
