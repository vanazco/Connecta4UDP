package com.company;

import java.io.Serializable;

class Tablero implements Serializable {

    private static String[][] tablero = new String[7][7];
    static boolean pintado = true;
    static int i =0;
    static int j =0;
    public static boolean turno = false;
    public static String guanyador = "";


    public static boolean isTurno() {
        return turno;
    }
    public static void setTurno(boolean turno) {
        Tablero.turno = turno;
    }

    static String getGuanyador() {
        return guanyador;
    }

    private static void setGuanyador(String guanyador) {
        Tablero.guanyador = guanyador;
    }


    Tablero(){
        if(pintado){
            rellenarTablero();
        }

    }

    boolean jugar() {
        pintarTurno();
        pintarTablero();
        return !guanyador.isEmpty();
    }

    private static void preguntarDeNuevo(){
        pintarTurno();
        pintarTablero();

    }

    private static void pintarTurno() {
        System.out.print("Turno de -> ");
        if (turno) {
            System.out.println("\033[31m X \033[0m");
        } else {
            System.out.println("\033[33m O \033[0m");
        }
    }

    private static void rellenarTablero() {
        for(i = 0; i < tablero.length; i++) {
            for (j = 0; j < tablero.length; j++) {
                tablero[i][j] = " - ";
            }
        }
        pintado = false;
    }

     public static void pintarTablero() {
        for (String[] strings : tablero) {
            for (j = 0; j < tablero.length; j++) {
                System.out.print(strings[j]);
                if (j == (tablero.length - 1)) {
                    System.out.println(" ");
                }
            }
        }
    }

    public void tirar(String columna){

        if (columna.isEmpty()) {
            System.out.println("No se ha escrito nada.");
            preguntarDeNuevo();
            return;
        }

        int posicion = Integer.parseInt(columna);

        if (posicion >= 8 || posicion < 1) {
            System.out.println("Input invalido.");
            preguntarDeNuevo();
            return;
        }
        
        comprovarPosicion(posicion, tablero);
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
class Tirada implements Serializable{
    String columna;
}
