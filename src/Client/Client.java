package Client;

import java.util.List;

public class Client {

    private String name;
    private int age;
    private int score;
    private boolean voteState;
    private List<String> cards;



    public Client(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public boolean requestHand() {
        return true;
    }

    public fillHand() {

    }
    public Cards pickCard(Cards card) {
        return card;
    }
    public int voteWinningHand() {
        return 0;
    }public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

}
