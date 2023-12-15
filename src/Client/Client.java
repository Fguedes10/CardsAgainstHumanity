package Client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    public void start(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
        new Thread(() -> {
            String messageFromServer = null;
            try {
                while ((messageFromServer = in.readLine()) != null) {
                    System.out.println(messageFromServer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println(this.name);
        String messageToServer = null;
        while ((messageToServer = consoleIn.readLine()) != null) {
            out.println(messageToServer);
        }
    }

    public void requestHand() {   //
    }
    public synchronized void fillHand(List<String> newCards) {
        if (cards.size() < 7) {
            int cardsNeeded = 7 - cards.size();
            List<String> cardsToAdd = newCards.subList(0, Math.min(cardsNeeded, newCards.size()));
            cards.addAll(cardsToAdd);
        }
    }


    public synchronized void pickCard(String card) {
        // Pick a card from hand to play
        // Implement logic for playing the card
        cards.remove(card);
    }
    /**
     * Synchronized method to vote for the winning hand.
     *
     * @throws IOException	if there is an I/O error while reading the vote
     */
   /* public synchronized void voteWinningHand() throws IOException {
        if (voteState) {
            int numberOfPlayers = getPlayeVVrCount();
            int[] votes = new int[numberOfPlayers];

            for (int i = 0; i < numberOfPlayers; i++) {
                int playerNumber = i + 1;
                System.out.println("Player " + playerNumber + ", enter the number of the card from another player that " +
                        "you think should win: ");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int vote = Integer.parseInt(reader.readLine());

                if (vote < 1 || vote > numberOfPlayers || vote == playerNumber) {
                    System.out.println("Invalid vote! Please enter a valid player number.");
                    i--;
                    continue;
                }
                votes[vote - 1]++;
            }

            int winningVote = -1;
            int winningVoteCount = 0;
            for (int i = 0; i < numberOfPlayers; i++) {
                if (votes[i] > winningVoteCount) {
                    winningVote = i + 1;
                    winningVoteCount = votes[i];
                }
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
            writer.write("The winning vote is for player " + winningVote);
            writer.newLine();
            writer.flush();
        }
    }

    */

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}










