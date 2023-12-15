package Client;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private int age;
    private int score;
    private List<String> cards;
    private boolean voteState;

    static final String SERVER_HOST = "localhost";
    static final int SERVER_PORT = 8080;

    static int numberOfConnections = 0;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        Client client = new Client("Client" + numberOfConnections,0);
        client.start(socket);
        numberOfConnections++;
    }

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
        this.score = 0;
        this.cards = new ArrayList<>();
        this.voteState = false;
    }

    public void start(Socket socket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(()->{
            String messageFromServer = null;
            try{
                while((messageFromServer = in.readLine()) != null){
                    System.out.println(messageFromServer);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }).start();
    }

    public void requestHand() {   //
    }
    public synchronized void fillHand(List<String> newCards) {
        // Verify if the number of cards in hand and fill hand
        if (cards.size() < 7) {
            cards.addAll(newCards);
        }
    }


    public synchronized void pickCard(String card) {
        // Pick a card from hand to play
        // Implement logic for playing the card
        cards.remove(card);
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










