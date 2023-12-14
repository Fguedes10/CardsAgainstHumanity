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
        this.cards = new List<>();
        this.voteState = false;
    }

    public void requestHand() {   //
    }

    public synchronized void fillHand() {   //verify if number of cards in hand and fill hand
    }

    public void pickCard() {  //pick card from hand to play
    }

    public void voteWinningHand() {  //if voteState true chose number of player to vote
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public Object getVoteState() {
        return voteState;
    }

    public boolean isVoteState() {
        return voteState;
    }

    public void setVoteState(boolean voteState) {
        this.voteState = voteState;
    }
}