package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The `Client` class represents a client with a name, age, score, cards, and vote state.
 * It also includes static fields for the server host, server port, and number of connections.
 */
public class Client {
    private String name;
    private int age;
    private int score;
    public List<String> cards;

    public List<String> getCards() {
        return cards;
    }

    private int maxHandSize = 7;
    private boolean voteState;

    public String playerVote = "";

    private String playedCard;

    public String getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(String playedCard) {
        this.playedCard = playedCard;
    }

    private ClientConnectionHandler correspondingClientConnectionHandlers;

    private boolean gameState = false;

    static final String SERVER_HOST = "localhost";
    static final int SERVER_PORT = 8500;
    static int numberOfConnections = 0;


    public Client() {
        this.name = null;
        this.age = 0;
        this.score = 0;
        this.cards = new CopyOnWriteArrayList<>();
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
        String messageToServer = null;
        while ((messageToServer = consoleIn.readLine()) != null) {
            out.println(messageToServer);
        }
    }

    private void chooseWhiteCard() {
        int randomCardPosition =
                new Random().nextInt(correspondingClientConnectionHandlers.getPlayingGame().getWhiteDeck().size());
        cards.add(correspondingClientConnectionHandlers.getPlayingGame().getWhiteDeck().remove(randomCardPosition));
        //return correspondingClientConnectionHandlers.getPlayingGame().getWhiteDeck().get(randomCardPosition);
    }

   /* public synchronized void fillHand(List<String> newCards) {
        if (cards.size() < 7) {
            int cardsNeeded = 7 - cards.size();
            List<String> cardsToAdd = newCards.subList(0, Math.min(cardsNeeded, newCards.size()));
            cards.addAll(cardsToAdd);
        }
    }*/

    public void fillHand(){
        for (int i = 1; i <= maxHandSize; i++) {
            chooseWhiteCard();
        }
        //correspondingClientConnectionHandlers.getPlayingGame().chooseBlackCard();
    }


    public synchronized String pickCard(int cardPosition) {
        return cards.remove(cardPosition);
    }
    /**
     * Synchronized method to vote for the winning hand.
     *
     * @throws IOException  if there is an I/O error while reading the vote
     */
    public synchronized void voteWinningHand() throws IOException {
        if (voteState) {
            int numberOfPlayers = 4; //TODO fetch number of players
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

    public void setCorrespondingClientConnectionHandler(ClientConnectionHandler clientConnectionHandler) {
        this.correspondingClientConnectionHandlers = clientConnectionHandler;
    }
}