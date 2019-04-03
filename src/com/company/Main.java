package com.company;

import java.io.IOException;

public class Main {
    private static boolean checkWinner = false;

    public static void main(String[] args) throws IOException {
        Tablero tablero = new Tablero();

        while (!checkWinner) {
            checkWinner = tablero.jugar();
        }

        System.out.println("Guanyador: " + Tablero.getGuanyador());
    }

}
