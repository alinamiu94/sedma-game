package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private  boolean isPreviosWinner;
    private int numberOfPoints;

    public String getName() {
        return name;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public boolean isPreviosWinner() {
        return isPreviosWinner;
    }

    public void setPreviosWinner(boolean previosWinner) {
        isPreviosWinner = previosWinner;
    }

    public void addPoints(int numberOfPoints) {
        this.numberOfPoints = this.numberOfPoints + numberOfPoints;
    }


    public Player(boolean isPreviosWinner, String name) {
        this.name = name;
        this.isPreviosWinner = isPreviosWinner;
        hand = new ArrayList<>();
        numberOfPoints = 0;

    }

    public void completeHand(List<Card> list) {
        if(hand.size() + list.size() > 4) {
            throw new IllegalArgumentException("Received list size is invalid");
        }
        hand.addAll(list);

    }

    public int getHandSize() {
        return hand.size();
    }

    public List<Card> getHand() {
        return hand;
    }

    public void printPlayerCards() {
        System.out.println(name + "'s cards ------------");
        for(int i = 1; i <= hand.size(); i++) {
            System.out.println(i + ". " + hand.get(i - 1));
        }
        System.out.println();
        System.out.println();
    }
    public Card extractCard(int i) {

        Card card = hand.get(i-1);
        hand.remove(i - 1);
        return card;
    }
    public Card getCard(int i) {

        return hand.get(i-1);
    }

}
