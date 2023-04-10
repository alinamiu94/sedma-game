package org.example;

import org.example.domain.Game;
import org.example.domain.Player;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player 1, what is you name?");
        String player1Name = scanner.next();

        System.out.println("Player 2, what is you name?");
        String player2Name = scanner.next();


        Player player1 = new Player(true, player1Name);
        Player player2 = new Player(false, player2Name);
        Game game1 = new Game(player1,player2);
        game1.init();
        game1.playGame();






    }
}