package com.company;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

class DatagramSocketClient {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private Tablero tablero;
    public int result;

    public void init(String host) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = 42069;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];

        while(true){
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

            try {
                socket.receive(packet);

                //processament de les dades rebudes i obtenció de la resposta
                result = getDataToRequest(packet.getData(), packet.getLength());
                tablero.jugar();

            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                result = -2;
            }
        }
    }

    public int getDataToRequest(byte[] data, int length) {

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            tablero = (Tablero) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tablero.code;
    }


    public static void main(String[] args) throws IOException {
        DatagramSocketClient socketClient = new DatagramSocketClient();

        String ipsrv;
        Scanner sc = new Scanner(System.in);

        System.out.print("Ip del servidor: ");
        ipsrv = sc.next();

        socketClient.init(ipsrv);
        socketClient.runClient();
    }
}
