package com.company;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

class DatagramSocketClient {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private Tablero tablero;


    void init(String host) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = 42069;
        socket = new DatagramSocket();
    }

    void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        //el servidor atén el port indefinidament
        while(tablero.code == 1){
            tablero.tirar();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(tablero);
            byte[] missatge = os.toByteArray();

            DatagramPacket packet = new DatagramPacket(missatge,
                    missatge.length,
                    serverIP,
                    serverPort);
            //enviament de la resposta
            socket.send(packet);

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);
            //espera de les dades

            try {
                socket.receive(packet);
                //processament de les dades rebudes i obtenció de la resposta
                tablero.code = getDataToRequest(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                tablero.code=0;
            }
        }
    }

    private int getDataToRequest(byte[] data, int length) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            tablero = (Tablero) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tablero.code;
    }
}
