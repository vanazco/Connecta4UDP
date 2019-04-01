package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {




    private final static String ColorPlayer1 = "\\u001B31;1m";

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        //INICIAR EL SERVER
//        DatagramSocketServer server = new DatagramSocketServer();
//
//        server.init();
//        server.runServer();


        String[][] tablero = new String[7][7];

        rellenarTablero(tablero);

        for (;;) {
            pintarTablero(tablero);
        }
    }

    private static void rellenarTablero(String[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = " - ";
            }
        }
    }

    private static void pintarTablero(String[][] tablero) throws IOException {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                System.out.print(tablero[i][j]);
                if (j == (tablero.length - 1)) {
                    System.out.println(" ");
                }
            }
        }

        System.out.println("");

        System.out.println(" 1  2  3  4  5  6  7");
        System.out.print("Escoge una columna: ");
        int leerPosicion = Integer.parseInt(br.readLine());
        comprovarPosicion(leerPosicion, tablero);
    }

    private static void comprovarPosicion(int leerPosicion, String[][] tablero) {
        leerPosicion -= 1;

        for (int i = 6; i >= 0; i--) {
            if (tablero[i][leerPosicion].matches(" - ")) {
                tablero[i][leerPosicion] = "\033[31m X \033[0m";
                break;
            }
        }
    }
}
