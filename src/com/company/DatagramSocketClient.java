package com.company;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

class DatagramSocketClient {
    private InetAddress serverIP;
    private int serverPort;
    private DatagramSocket socket;
    public Tirada tirada;
    public Tablero tablero;
    boolean terminar = false;
    int jugador;
    private boolean primera_jugada = true;


    public void init(String host) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = 42069;
        socket = new DatagramSocket();
        tirada = new Tirada();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        Scanner sc = new Scanner(System.in);

        System.out.print("Jugador 1 o 2: ");
        jugador = sc.nextInt();
        tirada.setJugador(jugador);
        sc.nextLine();

        while(!terminar){
            if(primera_jugada){
                System.out.println(" 1  2  3  4  5  6  7");
                System.out.print("Escoge una columna: ");
                tirada.columna = sc.nextLine();
                primera_jugada = false;
            }else {
                tablero.jugar();
                System.out.println(" 1  2  3  4  5  6  7");
                System.out.print("Escoge una columna: ");
                tirada.columna = sc.nextLine();
            }


            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(tirada);
            oos.flush();
            byte[] missatge = os.toByteArray();

            DatagramPacket packet = new DatagramPacket(missatge,
                    missatge.length,
                    serverIP,
                    serverPort);
            //enviament de la resposta
            socket.send(packet);


            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);


            socket.receive(packet);

            //processament de les dades rebudes i obtenció de la resposta
            getDataToRequest(packet.getData());

            if(tablero.turnoV){
                if(primera_jugada){
                    tablero.jugar();
                }
                if(!tablero.getGuanyador().equals("")){
                    terminar = true;
                    System.out.println("Ha ganado" +tablero.getGuanyador());
                }
            }else{
                if(primera_jugada){
                    tablero.jugar();
                }
                System.out.println("NO ES TU TURNO");
                if(!tablero.getGuanyador().equals("")){
                    terminar = true;
                    System.out.println("Ha ganado" +tablero.getGuanyador());
                }
            }
        }
    }

    public void getDataToRequest(byte[] data) {
        tablero = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            tablero = (Tablero) ois.readObject();
            tablero.jugar();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
