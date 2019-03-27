package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DatagramSocketServer {
    DatagramSocket socket;
    int random = (int)(Math.random() * ((50 - 1) + 1)) + 1;
    int code;
    int report;

    public void init() throws SocketException {
        socket = new DatagramSocket(42069);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[4];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;


        while(random!= report){

            DatagramPacket packet = new DatagramPacket(receivingData, 4);

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
        report = ByteBuffer.wrap(data).getInt();
        if(random > report){
            code = -1;
            byte[] codeb = ByteBuffer.allocate(4).putInt(code).array();
            return codeb;
        }else if (random < report){
            code = 1;
            byte[] codeb = ByteBuffer.allocate(4).putInt(code).array();
            return codeb;
        }else{
            code = 0;
            byte[] codeb = ByteBuffer.allocate(4).putInt(code).array();
            return codeb;
        }
    }
}
