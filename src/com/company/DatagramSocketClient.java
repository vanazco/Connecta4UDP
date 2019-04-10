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

    public void init(String host) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = 42069;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];

        tablero = new Tablero();
        tablero.code = 1;

        while(!checkWinner){
            checkWinner = !Tablero.getGuanyador().isEmpty();
            if (tablero.code == 1) {
                if(torn) {
                    tablero.jugar();
                    torn = false;
                    tablero.setTurno(torn);
                }else{
                    torn = tablero.isTurno();
                }
            }else{
                if(torn){
                    tablero.jugar();
                    torn = false;
                }else{
                    torn = tablero.isTurno();
                }
            }

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
            //socket.setSoTimeout(5000);
            //espera de les dades

            try {
                socket.receive(packet);
                //processament de les dades rebudes i obtenció de la resposta
                tablero.code = getDataToRequest(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                tablero.code = 0;
            }
        }
    }

    private int getDataToRequest(byte[] data, int length) {
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
