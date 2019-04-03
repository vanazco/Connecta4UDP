package com.company;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        DatagramSocketServer server = new DatagramSocketServer();
        DatagramSocketClient socketClient = new DatagramSocketClient();
        server.init();
        server.runServer();

        String ipsrv;
        Scanner sc = new Scanner(System.in);

        System.out.println("Ip del servidor?");
        ipsrv = sc.next();

        socketClient.init(ipsrv);
        socketClient.runClient();




    }

}
