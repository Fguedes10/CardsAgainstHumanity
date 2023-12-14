package Client;


import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private int age;
    private int score;
    private List<String> cards;
    private boolean voteState;

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
        this.score = 0;
        this.cards = new ArrayList<>();
        this.voteState = false;
    }

    public void requestHand() {   //
    }
    public synchronized void fillHand(List<String> newCards) {
        // Verify if the number of cards in hand and fill hand
        if (cards.size() < 4) {
            cards.addAll(newCards);
        }
    }


    public synchronized void pickCard(String card) {
        // Pick a card from hand to play
        if (cards.contains(card)) {
            cards.remove(card);
            // Implement logic for playing the card
        }
    }

    public synchronized void voteWinningHand() {
        // If voteState is true, choose the number of the player to vote
        if (voteState) {
            // Implement logic for voting
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isVoteState() {
        return voteState;
    }

    public void setVoteState(boolean voteState) {
        this.voteState = voteState;
    }
}