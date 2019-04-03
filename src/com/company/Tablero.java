package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

class Tablero implements Serializable {
    private static String[][] tablero = new String[7][7];

    private static boolean turno = false;
    private static String guanyador;
    public int code;
    public static boolean isTurno() {
        return turno;
    }
    public static void setTurno(boolean turno) {
        Tablero.turno = turno;
    }

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    Tablero() {
        rellenarTablero();
        guanyador = "";
    }

    static String getGuanyador() {
        return guanyador;
    }

    private static void setGuanyador(String guanyador) {
        Tablero.guanyador = guanyador;
    }

    boolean jugar() throws IOException {
        pintarTablero();
        tirar();
        return !guanyador.isEmpty();
    }

    private static void rellenarTablero() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = " - ";
            }
        }
    }

    static void pintarTablero() {
        for (String[] strings : tablero) {
            for (int j = 0; j < tablero.length; j++) {
                System.out.print(strings[j]);
                if (j == (tablero.length - 1)) {
                    System.out.println(" ");
                }
            }
        }
    }

    public static void tirar() throws IOException {
        System.out.println(" 1  2  3  4  5  6  7");
        System.out.print("Escoge una columna: ");
        String leer = br.readLine();

        if (leer.isEmpty()) {
            System.out.println("No se ha escrito nada.");
            pintarTablero();
            tirar();
            return;
        }

        int leerPosicion = Integer.parseInt(leer);

        if (leerPosicion >= 8 || leerPosicion < 1) {
            System.out.println("Input invalido.");
            pintarTablero();
            tirar();
            return;
        }
        
        comprovarPosicion(leerPosicion, tablero);
    }

    private static void comprovarPosicion(int leerPosicion, String[][] tablero) {
        leerPosicion -= 1;

        for (int i = 6; i >= 0; i--) {
            if (tablero[i][leerPosicion].matches(" - ")) {
                if (turno) {
                    tablero[i][leerPosicion] = "\033[31m X \033[0m";
                    turno = false;
                    break;
                } else {
                    tablero[i][leerPosicion] = "\033[33m O \033[0m";
                    turno = true;
                    break;
                }
            }
        }

        if (!turno) {
            if (areFourConnected("\033[31m X \033[0m", tablero))
                setGuanyador("\033[31m X \033[0m");
        } else {
            if (areFourConnected("\033[33m O \033[0m", tablero))
                setGuanyador("\033[33m O \033[0m");
        }
    }

    private static boolean areFourConnected(String player, String[][] tablero){

        // horizontalCheck
        for (int j = 0; j < tablero.length - 3 ; j++ ){
            for (int i = 6; i >= 0; i--){
                if (tablero[i][j].equals(player) && tablero[i][j+1].equals(player) && tablero[i][j+2].equals(player) && tablero[i][j+3].equals(player)){
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i< tablero.length-3 ; i++ ){
            for (int j = 0; j< tablero.length; j++){
                if (tablero[i][j].equals(player) && tablero[i+1][j].equals(player) && tablero[i+2][j].equals(player) && tablero[i+3][j].equals(player)){
                    return true;
                }
            }
        }
        // ascendingDiagonalCheck
        for (int i=3; i<tablero.length; i++){
            for (int j=0; j<tablero.length-3; j++){
                if (tablero[i][j].equals(player) && tablero[i-1][j+1].equals(player) && tablero[i-2][j+2].equals(player) && tablero[i-3][j+3].equals(player))
                    return true;
            }
        }
        // descendingDiagonalCheck
        for (int i=3; i<tablero.length; i++){
            for (int j=3; j<tablero.length; j++){
                if (tablero[i][j].equals(player) && tablero[i-1][j-1].equals(player) && tablero[i-2][j-2].equals(player) && tablero[i-3][j-3].equals(player))
                    return true;
            }
        }
        return false;
    }
}
