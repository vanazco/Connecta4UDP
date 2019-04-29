package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class DatagramSocketClient {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private Tablero tablero;
    private boolean checkWinner = false;
    private boolean torn = true;
    private String nom;

    public void init(String host) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = 42069;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];

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

            try {
                socket.receive(packet);
                //processament de les dades rebudes i obtenció de la resposta
                tablero.code = getDataToRequest(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                tablero.code = 0;
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

    public void setNom(String n){
        nom = n;
    }

    public static void main(String[] args) throws IOException {
        DatagramSocketClient socketClient = new DatagramSocketClient();

        String ipsrv,jugador;
        Scanner sc = new Scanner(System.in);

        System.out.print("Ip del servidor: ");
        ipsrv = sc.next();
        System.out.println("nom de jugador: ");
        jugador = sc.next();

        socketClient.setNom(jugador);


        socketClient.init(ipsrv);
        socketClient.runClient();
    }
}
