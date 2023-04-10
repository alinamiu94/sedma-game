package org.example.domain;

import java.util.*;

public class Game {
    private List<Card> roundCards;
    private Player player1;
    private Player player2;
    private List<Card> stock;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        stock = new ArrayList<>();
        roundCards = new ArrayList<>();
    }

    public void init() {
        populateStock();
        shuffle();
        completeHand(player1,player2);
    }

    private void populateStock() {

        for (CardValue cardvalue : CardValue.values()) {
            boolean isPoint;
            boolean isWildCard;

            if (cardvalue.equals(CardValue.TEN) || cardvalue.equals(CardValue.ACE)) {
                isPoint = true;
            } else {
                isPoint = false;
            }

            if (cardvalue.equals(CardValue.SEVEN)) {
                isWildCard = true;
            } else {
                isWildCard = false;
            }

            for (Suit suit : Suit.values()) {
                Card card = new Card(cardvalue, suit, isPoint, isWildCard);
                stock.add(card);
            }
        }
    }

    private void shuffle() {
        List<Integer> randomCards = new ArrayList<>();

        while (randomCards.size() < 32) {
            int shuffleCard = (int)(Math.random()* 32);
            if(!randomCards.contains(shuffleCard)) {
                randomCards.add(shuffleCard);
            }
        }
        List<Card> shuffledCards = new ArrayList<>();
        for(Integer randomNumber: randomCards) {
            shuffledCards.add(stock.get(randomNumber));
        }
        stock = shuffledCards;
    }

    public void completeHand(Player player1, Player player2) {
        if (stock.size() == 0){
            return;
        }

        int numberOfCurrentCards = player1.getHandSize();
        int numberOfCardsToGet;
        if((numberOfCurrentCards == 1 && stock.size() < 6) ||
                (numberOfCurrentCards == 2 && stock.size() < 4) ||
                (numberOfCurrentCards == 3 && stock.isEmpty())) {
            numberOfCardsToGet = stock.size() / 2;
        } else {
            numberOfCardsToGet = 4 - numberOfCurrentCards;
        }
        List<Card> newRoundCards = new ArrayList<>();
        for(int i = 0; i < numberOfCardsToGet; i++) {
            newRoundCards.add(stock.get(0));
            stock.remove(0);
        }
        player1.completeHand(newRoundCards);
        newRoundCards = new ArrayList<>();
        for(int i = 0; i < numberOfCardsToGet; i++) {
            newRoundCards.add(stock.get(0));
            stock.remove(0);
        }
        player2.completeHand(newRoundCards);

    }

    public boolean hasNotEnteredAValidInput(String choice) {
        try {
            int choiceInt = Integer.parseInt(choice);
            if(choiceInt <= Math.max(player1.getHandSize(), player2.getHandSize()) && choiceInt >= 1) {
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return true;
        }
    }

    public void playSmallRound(Player p, boolean isForcedToCutTheRound) {
        Scanner scanner = new Scanner(System.in);
        String choice = null;
        Card card;

        p.printPlayerCards();
        boolean hasCutTheRound = false;
        boolean hasNotEnteredAValidInput = hasNotEnteredAValidInput(choice);

        while (hasNotEnteredAValidInput || (isForcedToCutTheRound && !hasCutTheRound) ) {
            choice = scanner.next();
            hasNotEnteredAValidInput = hasNotEnteredAValidInput(choice);
            if(hasNotEnteredAValidInput) {
                System.out.println("You have entered wrong input. Please enter again a valid input!");
                continue;
            }
            int choiceInt = Integer.parseInt(choice);
            card = p.getCard(choiceInt);
            if(isForcedToCutTheRound) {
                if(!(card.isWildCard() || card.getValue().equals(roundCards.get(0).getValue()))) {
                    System.out.println("You have not entered a wildcard or the same value of the first card!");
                    p.completeHand(List.of(card));
                    continue;
                } else {
                    hasCutTheRound = true;
                }
            }
            card = p.extractCard(choiceInt);
            roundCards.add(card);
        }

        printRoundCards();
    }

    public void endOfRound() {
        int numberOfPoints = countRoundPoints();
        getWinner().addPoints(numberOfPoints);
        completeHand(player2,player1);
        Player winner = getWinner();
        if(winner.equals(player1)) {
            player1.setPreviosWinner(true);
            player2.setPreviosWinner(false);
        } else {
            player1.setPreviosWinner(false);
            player2.setPreviosWinner(true);
        }
        roundCards = new ArrayList<>();

    }

    public Player getWinner() {
        if(player1.isPreviosWinner() &&  (roundCards.get(roundCards.size() - 1).isWildCard() ||
                roundCards.get(roundCards.size() - 1).getValue().equals(roundCards.get(0).getValue()))) {
            return player2;
        }
        if(player1.isPreviosWinner() && !(roundCards.get(roundCards.size() - 1).isWildCard() ||
                roundCards.get(roundCards.size() - 1).getValue().equals(roundCards.get(0).getValue()))) {
            return player1;
        }
        if(player2.isPreviosWinner() &&  (roundCards.get(roundCards.size() - 1).isWildCard() ||
                roundCards.get(roundCards.size() - 1).getValue().equals(roundCards.get(0).getValue()))) {
            return player1;
        }
        if(player2.isPreviosWinner() &&  !(roundCards.get(roundCards.size() - 1).isWildCard() ||
                roundCards.get(roundCards.size() - 1).getValue().equals(roundCards.get(0).getValue()))) {
            return player2;
        }
        return null;
    }

    public Player getPreviousWinner() {
        if(player2.isPreviosWinner()) {
            return player2;
        } else {
            return player1;
        }
    }

    public Player getPreviousLoser() {
        if(!player1.isPreviosWinner()) {
            return player1;
        } else {
            return player2;
        }
    }

    public void playRound() {
        playSmallRound(getPreviousWinner(), false);
        playSmallRound(getPreviousLoser(), false);
        while (canRoundContinue()) {
            System.out.println("Do you want to continue this round? yes/no");

            Scanner scanner = new Scanner(System.in);
            if(scanner.next().equals("yes")) {
                playSmallRound(getPreviousWinner(), true);
                playSmallRound(getPreviousLoser(), false);
            } else {
                break;
            }
        }
        endOfRound();
    }

    public void playGame() {
        while (player1.getHandSize() > 0) {
            playRound();
        }
        System.out.println(player2.getName()  + " has " + player2.getNumberOfPoints() + " points.");
        System.out.println(player1.getName()  + " has " + player1.getNumberOfPoints() + " points.");

        if(player1.getNumberOfPoints() > player2.getNumberOfPoints()) {
            System.out.println(player1.getName() + " won!!!");
        } else if (player1.getNumberOfPoints() == player2.getNumberOfPoints()) {
            System.out.println("Tie!!");
        } else {
            System.out.println(player2.getName() + " won!!!");
        }
    }

    public int countRoundPoints() {
        int count = 0;
        for(Card card:roundCards) {
            if(card.isPoint()) {
                count +=1;
            }
        }
        return count;
    }

    public boolean canRoundContinue() {
        if(roundCards.size() == 8) {
            return false;
        }
        if(!(roundCards.get(roundCards.size() - 1).isWildCard() ||
                roundCards.get(roundCards.size() - 1).getValue().equals(roundCards.get(0).getValue()))) {
            return false;
        }
        if(player1.isPreviosWinner()) {
            List<Card> remaningCards = player1.getHand();
            for(Card card:remaningCards) {
                if(card.isWildCard() || card.getValue().equals(roundCards.get(0).getValue())) {
                    return true;
                }
            }
            return false;
        }
        if(player2.isPreviosWinner()) {
            List<Card> remaningCards = player2.getHand();
            for(Card card:remaningCards) {
                if(card.isWildCard() || card.getValue().equals(roundCards.get(0).getValue())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void printRoundCards() {
        for(Card card:roundCards) {
            System.out.print(card + " ");
        }
        System.out.println();
    }
}
