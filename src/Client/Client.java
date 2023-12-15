package Client;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The `Client` class represents a client with a name, age, score, cards, and vote state.
 * It also includes static fields for the server host, server port, and number of connections.
 */
public class Client {
    private String name;
    private int age;
    private int score;
    private List<String> cards;
    private boolean voteState;

    static final String SERVER_HOST = "localhost";
    static final int SERVER_PORT = 8080;

    static int numberOfConnections = 0;

    /**
     * Main function that starts the client connection to the server.
     *
     * @param  args  the command-line arguments
     * @throws IOException  if an I/O error occurs when creating the socket
     */
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

    /**
     * Starts the socket connection and initializes the input and output streams.
     *
     * @param  socket  the socket connection to start
     * @throws IOException  if an I/O error occurs when creating the input and output streams
     */
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

    /**
     * Retrieves the score.
     *
     * @return the score value
     */
    public int getScore() {
        return score;
    }


    /**
     * Sets the score.
     *
     * @param  score  the new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves the current vote state.
     *
     * @return  true if the vote state is active, false otherwise
     */
    public boolean isVoteState() {
        return voteState;
    }

    /**
     * Sets the vote state of the object.
     *
     * @param  voteState  the new vote state to be set
     */
    public void setVoteState(boolean voteState) {
        this.voteState = voteState;
    }
}










