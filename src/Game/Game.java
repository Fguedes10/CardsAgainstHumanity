package Game;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;
import Client.Client;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * The `Game` class represents a game of Cards Against Mindera.
 * It manages game state, players, card decks, rounds, and scoring.
 */
public class Game {

    /**
     * The name of the game.
     */
    public String name;

    /**
     * The maximum number of players allowed in the game.
     */
    public int maxNumOfPlayers;

    /**
     * The list of players participating in the game.
     */
    public ArrayList<ClientConnectionHandler> players = new ArrayList<>();
    /**
     * The list of cards submitted for the current round.
     */
    public List<String> roundCardsToVote;
    /**
     * The owner of the game.
     */
    public ClientConnectionHandler owner;
    /**
     * The score required to win the game.
     */
    public int scoreToWin = 7;
    /**
     * The current state of the game (true if the game has started).
     */
    public boolean state = false;
    /**
     * The counter for played cards in the current round.
     */
    private int playedCardsCounter = 0;
    /**
     * The current round number.
     */
    private int currentRound = 0;
    /**
     * Flag indicating whether the game is over.
     */
    private boolean isGameOver = false;
    /**
     * The black card in the current game round.
     */
    private String blackCardInGame;
    /**
     * The list of white cards available in the game.
     */
    public List<String> cardsInGame = initializeCardsInGame();

    /**
     * Increment the played cards counter.
     */
    public synchronized void incrementPlayedCardsCounter() {
        playedCardsCounter++;
    }

    /**
     * Check if all players have played their cards for the current round.
     *
     * @return true if all players have played, false otherwise
     */
    public synchronized boolean allPlayersPlayedCards() {
        return playedCardsCounter == maxNumOfPlayers;
    }

    /**
     * Initializes an empty list of cards in the game.
     *
     * @return An empty list of cards in the game.
     */
    private List<String> initializeCardsInGame() {
        List<String> cardsInGame = new ArrayList<>();
        return cardsInGame;
    }

    /**
     * The list of white cards used in the game.
     */
    private List<String> whiteDeck = initializeWhiteDeck();

    /**
     * The list of black cards used in the game.
     */

    private List<String> blackDeck = initializeBlackDeck();

    /**
     * A mapping of submitted cards to the corresponding player.
     */
    public Map<String, ClientConnectionHandler> cardSubmissions = new HashMap<>();

    /**
     * The list of currently running game instances.
     */
    public static List<Game> runningGames = new LinkedList<>();

    /**
     * Checks if all players have joined the game, updates the game state accordingly, and returns the result.
     *
     * @param game The game to check.
     * @return True if all players have joined, false otherwise.
     */
    public boolean checkAllPlayersInGame(Game game) {
        if (game.players.size() == game.maxNumOfPlayers) {
            game.state = true;
            return true;
        }
        return false;
    }

    /**
     * Creates a new game with the specified owner, maximum number of players, and name.
     *
     * @param owner           The owner of the game.
     * @param maxNumOfPlayers The maximum number of players allowed in the game.
     * @param name            The name of the game.
     * @throws IOException If an I/O error occurs.
     */
    public Game(ClientConnectionHandler owner, int maxNumOfPlayers, String name) throws IOException {
        this.owner = owner;
        this.name = name;
        this.maxNumOfPlayers = maxNumOfPlayers;
        runningGames.add(this);
        players.add(this.owner);
        System.out.println(players);
        this.roundCardsToVote = new ArrayList<>();

    }

    /**
     * Announces the result of the vote for a specific card in the game.
     *
     * @param card The winning card.
     * @param name The name of the winning player.
     * @throws IOException If an I/O error occurs.
     */

    public void announceVoteResult(String card, String name) throws IOException {
        Server.announceInGame(Messages.WHITE_BOLD +"\n" + name + Messages.ROUND_WINNER + card + "\n" + Messages.RESET_COLOR, this);
    }

    /**
     * Submits a card for a specific player in the game.
     *
     * @param card   The card to be submitted.
     * @param player The player submitting the card.
     */
    public void submitCard(String card, ClientConnectionHandler player) {
        cardSubmissions.put(card, player);
    }


    /**
     * Retrieves the player who submitted a specific card.
     *
     * @param card The card for which to retrieve the submitting player.
     * @return The submitting player or null if the card is not found.
     */
    public ClientConnectionHandler getSubmittingPlayer(String card) {
        return cardSubmissions.get(card);
    }

    /**
     * Resets the state of the game round, clearing played cards, card submissions, and related variables.
     * Additionally, sets the vote state and round card score to their initial values for all players.
     */
    private void resetGameRound() {
        playedCardsCounter = 0;
        cardSubmissions.clear();
        cardsInGame.clear();
        roundCardsToVote.clear();
        for (ClientConnectionHandler player : players) {
            player.getCorrespondingClient().setVoteState(false);
            player.getCorrespondingClient().setRoundCardScore(0);
        }
    }

    /**
     * Checks for a winner by counting the number of players with an empty card hand.
     * If the count equals the maximum number of players, it indicates that all players have won.
     */
    public void checkWinner() {
        int counter = 0;
        for (ClientConnectionHandler player : players) {
            if (player.getCorrespondingClient().getCards().isEmpty()) {
                counter++;
            }
        }
        if (counter == maxNumOfPlayers) {
        }
    }

    /**
     * Announces the big winner of the game by checking each player's score.
     * If a player's score matches the predefined scoreToWin, they are declared the winner.
     * The game state is updated to indicate that the game is over if a winner is found.
     */
    public void announceBigWinner() {
        for (ClientConnectionHandler player : players) {
            if (player.getCorrespondingClient().getScore() == scoreToWin) {
                Server.announceInGame(Messages.BLUE_BOLD + Messages.GAME_WINNER + player.getName() + Messages.RESET_COLOR, this);
                isGameOver = true;
            }
        }
    }

    /**
     * Retrieves a formatted string containing information about all running games.
     * Each line in the output represents a game and includes details such as the name,
     * the maximum number of players, the number of players in the game, available free spots,
     * and the name of the player who started the game.
     *
     * @return A string containing information about all running games.
     * @throws IOException If an I/O error occurs while retrieving game information.
     */
    public static String getRunningGames() throws IOException {
        StringBuffer buffer = new StringBuffer();
        runningGames.forEach(game -> buffer.append(game.name)
                .append(" - ").append(game.maxNumOfPlayers)
                .append(" player game with ").append(game.maxNumOfPlayers - game.players.size())
                .append(" free spots ").append("started by ")
                .append(game.owner.getName()).append("\n"));
        return buffer.toString();
    }


    /**
     * Retrieves a game based on its name.
     *
     * @param name The name of the game.
     * @return The game with the specified name, or null if not found.
     */
    public static Game getGameByName(String name) {
        for (int i = 0; i < runningGames.size(); i++) {
            if (runningGames.get(i).name.equalsIgnoreCase(name)) {
                return runningGames.get(i);
            }
        }
        return null;
    }

    /**
     * Initiates the start of a game, including announcements and setup procedures.
     *
     * @param game The game to start.
     * @throws InterruptedException If the thread is interrupted during execution.
     * @throws IOException          If an I/O error occurs.
     */
    public void startGame(Game game) throws InterruptedException, IOException {
        Server.announceInGame(Messages.EXITING_LOBBY, game);
        announceStartOfGame(game);
        clearScreen(game);
        clearScreen(game);
        Server.announceInGame(Messages.GAME_BEGINS, game);
    }


    /**
     * Announces the start of a new round, resets game-related counters, and provides information to players.
     *
     * @throws IOException If an I/O error occurs during the announcement.
     */

    public void announceStartOfNewRound() throws IOException {

        resetGameRound();
        Server.announceInGame(Messages.YELLOW_BOLD + "SCOREBOARD: \n", this);

        for (ClientConnectionHandler player : players) {
            Server.announceInGame(Messages.YELLOW_BOLD + player.getName() + " - " + player.getCorrespondingClient().getScore(), this);
        }
        currentRound++;
        chooseBlackCard();
        Server.announceInGame(Messages.ROUND + currentRound, this);
        Server.announceInGame("This turn's Black Card is:\n" + Card.drawBlackCard(blackCardInGame), this);

    }

    /**
     * Chooses a black card for the current round.
     */
    private void chooseBlackCard() {
        int randomCardPosition = new Random().nextInt(blackDeck.size());
        blackCardInGame = blackDeck.get(randomCardPosition);
    }

    /**
     * Announces the start of the game by sending countdown messages to players.
     *
     * @param game The game about to start.
     * @throws InterruptedException If the thread is interrupted during execution.
     */
    private void announceStartOfGame(Game game) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            for (ClientConnectionHandler client : game.players) {
                try {
                    client.writeMessage(Messages.GAME_STARTING + (10 - i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Thread.sleep(1000);
        }
    }

    /**
     * Clears the screen for all players in the game.
     *
     * @param game The game for which the screen is cleared.
     */

    private void clearScreen(Game game) {
        for (ClientConnectionHandler player : game.players) {
            Server.announceInGame("\033[H\033[2J", game);
            System.out.flush();
        }
    }

    /**
     * Allows a player to join the game and sends a corresponding message.
     *
     * @param clientConnectionHandler The connection handler of the joining player.
     * @throws IOException If an I/O error occurs while sending the join message.
     */
    public void join(ClientConnectionHandler clientConnectionHandler) throws IOException {
        players.add(clientConnectionHandler);
        clientConnectionHandler.writeMessage(Messages.JOINED_GAME + this.name);
    }

    /**
     * Initializes the white deck of cards by reading content from a file.
     *
     * @return The list of strings representing the white deck.
     * @throws IOException If an I/O error occurs during deck initialization.
     */
    public List<String> initializeWhiteDeck() throws IOException {
        Path path = Paths.get("src/Decks/whiteDeck.txt");
        return Files.readAllLines(path);
    }

    /**
     * Initializes the black deck of cards by reading content from a file.
     *
     * @return The list of strings representing the black deck.
     * @throws IOException If an I/O error occurs during deck initialization.
     */
    public List<String> initializeBlackDeck() throws IOException {
        Path path = Paths.get("src/Decks/blackDeck.txt");
        return Files.readAllLines(path);
    }

    /**
     * Checks if all players in the game have voted.
     *
     * @return True if all players have voted, false otherwise.
     */
    public synchronized boolean allPlayersVoted() {
        for (ClientConnectionHandler player : players) {
            if (player.getCorrespondingClient().isVoteState()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Handles the result of the voting phase, determines the winner, and initiates the next round.
     *
     * @throws IOException If an I/O error occurs during result announcement.
     */
    public void handleVotingResult() throws IOException {
        List<Client> clientList = new ArrayList<>();
        for (ClientConnectionHandler player : players) {
            clientList.add(player.getCorrespondingClient());
        }

        List<Client> playerScoresSorted = clientList.stream()
                .sorted(Comparator.comparingInt(Client::getScore).reversed())
                .toList();

        List<Client> winners = new ArrayList<>();
        int highestScore = playerScoresSorted.get(0).getScore();

        for (Client player : playerScoresSorted) {
            if (player.getScore() == highestScore) {
                winners.add(player);
            } else {
                break;
            }
        }


        Client winningPlayer = getRandomWinner(winners);

        if (winningPlayer != null) {

            winningPlayer.incrementScore();

            announceVoteResult(winningPlayer.getPlayedCard(), winningPlayer.getName());

        }
        announceBigWinner();
        if (isGameOver) {
            Server.announceInGame(Messages.GAME_OVER, this);
            return;
        }
        announceStartOfNewRound();
    }

    /**
     * Selects a random winner from a list of players.
     *
     * @param players The list of players.
     * @return The randomly selected winning player.
     */
    private Client getRandomWinner(List<Client> players) {
        if (!players.isEmpty()) {
            Random random = new Random();
            return players.get(random.nextInt(players.size()));
        }
        return null;
    }

    /**
     * Initiates the voting phase by sending relevant instructions and card information to players.
     *
     * @param clientConnectionHandler The connection handler of the player initiating the voting phase.
     * @throws IOException If an I/O error occurs during the announcement.
     */
    public void startVotingPhase(ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.getPlayingGame().setPlayedCardsCounter(0);
        Server.announceInGame(Messages.VOTING_PHASE_START, clientConnectionHandler.getPlayingGame());
        Server.announceInGame(Messages.VOTING_INSTRUCTIONS, clientConnectionHandler.getPlayingGame());
        int maxLines = 0;
        List<List<String>> cardLinesList = new ArrayList<>();
        for (ClientConnectionHandler player : clientConnectionHandler.getPlayingGame().players) {
            Map<String, ClientConnectionHandler> cardsMap = clientConnectionHandler.getPlayingGame().cardSubmissions;
            List<String> cards = cardsMap.keySet().stream().filter(card -> !card.equalsIgnoreCase(player.getCorrespondingClient().getPlayedCard())).toList();
            for (String card : cards) {
                List<String> cardLines = Card.drawHand(card, cardsInGame.indexOf(card) + 1);
                cardLinesList.add(cardLines);
                maxLines = Math.max(maxLines, cardLines.size());
            }

            // Construct each line of the hand
            for (int i = 0; i < maxLines; i++) {
                StringBuilder handLine = new StringBuilder();
                for (List<String> cardLines : cardLinesList) {
                    handLine.append(cardLines.size() > i ? cardLines.get(i) : " ".repeat(22)); // 22 is the card width
                }
                player.writeMessage(handLine.toString());
            }
            cardLinesList.clear();
        }
    }


    /**
     * Retrieves the white deck of cards.
     *
     * @return The white deck of cards.
     */
    public List<String> getWhiteDeck() {
        return whiteDeck;
    }


    /**
     * Adds a card to the list of cards in the current game.
     *
     * @param card The card to be added.
     */
    public void setCardsInGame(String card) {
        cardsInGame.add(card);
    }

    /**
     * Sets the counter for the number of played cards in the current round.
     *
     * @param playedCardsCounter The new value for the played cards counter.
     */
    public void setPlayedCardsCounter(int playedCardsCounter) {
        this.playedCardsCounter = playedCardsCounter;
    }

}