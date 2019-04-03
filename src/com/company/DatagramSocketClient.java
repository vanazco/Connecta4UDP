package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;

class DatagramSocketClient {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private int intentos = 3;
    private boolean finalitzar = false;

    void init(String host, int port) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    void runClient() throws IOException {
        byte [] receivedData = new byte[4];
        int intentos = 3;

        System.out.println("MÁXIM INTENTS -> 3");
        //el servidor atén el port indefinidament
        while(!finalitzar){
            System.out.print("Escriu el numero (intents " + intentos + "): ");
            int numero = Integer.parseInt(br.readLine());

            byte[] numeroBytes =  ByteBuffer.allocate(4).putInt(numero).array();

            DatagramPacket packet = new DatagramPacket(numeroBytes,
                    numeroBytes.length,
                    serverIP,
                    serverPort);
            //enviament de la resposta
            socket.send(packet);

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 4);
            //espera de les dades
            socket.receive(packet);
            //processament de les dades rebudes i obtenció de la resposta
            numeroBytes = packet.getData();

            int numServer = ByteBuffer.wrap(numeroBytes).getInt();

            if (numServer == 1) {
                System.out.println("El numero es més petit.");
                reducirIntento();
            } else if (numServer == -1) {
                System.out.println("El numero es més gran.");
                reducirIntento();
            } else {
                System.out.println("Has guanyat!");
                finalitzar = true;
            }
        }
    }

    private void reducirIntento() {
        intentos--;
        if (intentos <= 0) {
            System.out.println("Has perdut...");
            finalitzar = true;
        } else {
            System.out.println("Numero d'intents restants: " + intentos);
        }
    }
}
