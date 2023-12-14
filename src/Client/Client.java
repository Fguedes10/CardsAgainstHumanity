package Client;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private int age;
    private int score;
    private List<Card> cards;
    private Object voteState;

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
        this.score = 0;
        this.cards = new ArrayList<>();
        this.voteState = null;
    }

    public void requestHand() {
    }

    public synchronized void fillHand() {
    }

    public void pickCard() {
    }

    public void voteWinningHand() {
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Object getVoteState() {
        return voteState;
    }

    public void setVoteState(Object voteState) {
        this.voteState = voteState;
    }
}