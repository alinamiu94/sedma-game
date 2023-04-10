package org.example.domain;

public class Card {
    private CardValue value;
    private Suit suit;
    private boolean isPoint;
    private boolean isWildCard;

    Card(CardValue value, Suit suit, boolean isPoint, boolean isWildCard) {
        this.value = value;
        this.suit = suit;
        this.isPoint = isPoint;
        this.isWildCard = isWildCard;
    }

    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public void setPoint(boolean point) {
        isPoint = point;
    }

    public boolean isWildCard() {
        return isWildCard;
    }

    public void setWildCard(boolean wildCard) {
        isWildCard = wildCard;
    }

    @Override
    public String toString() {
        return value + " " + suit.toString();
    }
}
