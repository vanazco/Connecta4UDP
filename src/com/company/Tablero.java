package com.company;

import java.io.Serializable;

class Tablero implements Serializable {

    public String[][] tablero = new String[7][7];
    public int turno = 1;
    public String guanyador = "";
    public boolean turnoV = true;



    public String getGuanyador() {
        return guanyador;
    }

    public  void setGuanyador(String guanyador) {
        this.guanyador = guanyador;
    }


    Tablero(){
            rellenarTablero();
    }

    boolean jugar() {
        pintarTurno();
        pintarTablero();
        return !guanyador.isEmpty();
    }

    private void preguntarDeNuevo(){
        pintarTurno();
        pintarTablero();

    }

    private void pintarTurno() {
        System.out.print("Turno de -> ");
        if (turno == 1) {
            System.out.println("\033[31m X \033[0m");
        } else {
            System.out.println("\033[33m O \033[0m");
        }
    }

    private void rellenarTablero() {
        for(int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = " - ";
            }
        }
    }

     public void pintarTablero() {
        for (String[] strings : tablero) {
            for (int j = 0; j < tablero.length; j++) {
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

    private void comprovarPosicion(int leerPosicion, String[][] tablero) {
        leerPosicion -= 1;

        for (int i = 6; i >= 0; i--) {
            if (tablero[i][leerPosicion].matches(" - ")) {
                if (turno == 1) {
                    tablero[i][leerPosicion] = "\033[31m X \033[0m";
                    break;
                } else {
                    tablero[i][leerPosicion] = "\033[33m O \033[0m";
                    break;
                }
            }
        }

        if (turno != 1) {
            if (areFourConnected("\033[31m X \033[0m", tablero))
                setGuanyador("\033[31m X \033[0m");
        } else {
            if (areFourConnected("\033[33m O \033[0m", tablero))
                setGuanyador("\033[33m O \033[0m");
        }
    }

    private boolean areFourConnected(String player, String[][] tablero){

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
    int jugador;

    public void setJugador(int jugador) {
        this.jugador = jugador;
    }

}
