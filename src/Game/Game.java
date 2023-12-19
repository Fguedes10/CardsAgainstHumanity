package Game;

import Client.ClientConnectionHandler;
import Client.Client;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public String name;
    public int maxNumOfPlayers;
    public ArrayList<ClientConnectionHandler> players = new ArrayList<>();
    public List<String> roundCardsToVote;
    public ClientConnectionHandler owner;
    public int scoreToWin = 7;
    public int numberOfInGamePlayers;
    public boolean state = false;
    private int playedCardsCounter = 0;
    private int currentRound = 0;

    public synchronized void incrementPlayedCardsCounter() {
        playedCardsCounter++;
    }

    public synchronized boolean allPlayersPlayedCards() {
        return playedCardsCounter == maxNumOfPlayers;
    }

    private String blackCardInGame;

    public String getBlackCardInGame() {
        return blackCardInGame;
    }

    public List<String> cardsInGame = initializeCardsInGame();

    private List<String> initializeCardsInGame() {
        List<String> cardsInGame = new ArrayList<>();
        return cardsInGame;
    }

    public List<String> getCardsInGame() {
        return cardsInGame;
    }

    private List<String> whiteDeck = initializeWhiteDeck();

    private List<String> blackDeck = initializeBlackDeck();

    public static List<Game> runningGames = new LinkedList<>();

    public boolean checkAllPlayersInGame(Game game) {
        if (game.players.size() == game.maxNumOfPlayers) {
            game.state = true;
            return true;
        }
        return false;
    }

    public Game(ClientConnectionHandler owner, int maxNumOfPlayers, String name) throws IOException {
        this.owner = owner;
        this.name = name;
        this.maxNumOfPlayers = maxNumOfPlayers;
        runningGames.add(this);
        players.add(this.owner);
        System.out.println(players);
        this.roundCardsToVote = new ArrayList<>();

    }

    /*public void findClientByCard(String playedCard){
        players.stream().filter(player -> player.getCorrespondingClient())
    }

     */

    public void announceVoteResult() throws IOException {
        //int votesForCard = 0;
        Map<ClientConnectionHandler, Integer> votePerCard = new HashMap<>();
        List<ClientConnectionHandler> players = new ArrayList<>();
        for (ClientConnectionHandler player : players) {
            String playerVote = player.getCorrespondingClient().playerVote;
            if (votePerCard.containsKey(playerVote)) {
                votePerCard.put(player, votePerCard.get(playerVote).getValue() + 1);
            } else {
                votePerCard.put(player, 1);
            }
//            if (!player.getCorrespondingClient().equals(owner.getCorrespondingClient()) &&
//                    player.getCorrespondingClient().playerVote.equalsIgnoreCase(player.getPlayingGame().roundCardsToVote.get(0))) {
//                votesForCard++;
//            }
        }
        Map.Entry<ClientConnectionHandler, Integer> maxVote = Collections.max(votePerCard.entrySet(), Map.Entry.comparingByValue());
        if (maxVote.getValue() > players.size() / 2) {
            // The voted card wins
            // owner.send("The winning card is: " + roundCardsToVote.get(0));
            maxVote.getKey().send("The winning card is: " + maxVote.getKey());
            maxVote.getKey().getCorrespondingClient().setScore(maxVote.getKey().getCorrespondingClient().getScore() + 1);
        } else {
            owner.send("No consensus on the winning card.");
        }
        resetGameRound();
        startNewRound(); // TODO start new round
    }

    private void resetGameRound() {
        roundCardsToVote.clear();
        for (ClientConnectionHandler player : players) {
            player.getCorrespondingClient().setVoteState(false);
        }
    }

    public void checkWinner(){
        int counter = 0;
      for(ClientConnectionHandler player : players){
          if(player.getCorrespondingClient().getCards().isEmpty()){
              counter++;
          }
      }
      if(counter == maxNumOfPlayers){
          announceWinner();
      }
    }

    private void announceWinner() {
        String winner = players.stream().filter(player -> player.getCorrespondingClient().getScore() >= scoreToWin).map(player -> player.getCorrespondingClient().getName()).findFirst().toString();
        Server.announceInGame("The winner of the cookie is: " + winner,this);
    }

    public static String getRunningGames() throws IOException {
        StringBuffer buffer = new StringBuffer();
        runningGames.forEach(game -> buffer.append(game.name)
                .append(" - ").append(game.maxNumOfPlayers)
                .append(" player game with ").append(game.maxNumOfPlayers - game.players.size())
                .append(" free spots ").append("started by ")
                .append(game.owner.getName()).append("\n"));
        return buffer.toString();
    }

    public static Game getGameByOwner(ClientConnectionHandler clientConnectionHandler) {
        for (int i = 0; i < runningGames.size(); i++) {
            if (runningGames.get(i).owner.toString().equalsIgnoreCase(clientConnectionHandler.getName())) {
                return runningGames.get(i);
            }
        }
        return null;
    }

    public static void askForCard(Client client, String card) {

    }

    public static Game getGameByName(String name) {
        for (int i = 0; i < runningGames.size(); i++) {
            if (runningGames.get(i).name.equalsIgnoreCase(name)) {
                return runningGames.get(i);
            }
        }
        return null;
    }

    public void startGame(Game game) throws InterruptedException, IOException {
        Server.announceInGame(Messages.EXITING_LOBBY, game);
        announceStartOfGame(game);
        //Inserir lógica do jogo
        clearScreen(game);
        Server.announceInGame(Messages.GAME_BEGINS, game);
    }


    public void announceStartOfNewRound() throws IOException {
        Server.announceInGame("SCOREBOARD: \n", this);

        for (ClientConnectionHandler player : players){
            Server.announceInGame(player.getName()  + " - " + player.getCorrespondingClient().getScore(), this);
        }
            currentRound++;
            chooseBlackCard();
            Server.announceInGame(Messages.ROUND + currentRound, this);
            Server.announceInGame("This turn's Black Card is:\n" + blackCardInGame, this);

    }

    private void chooseBlackCard() {
        int randomCardPosition = new Random().nextInt(blackDeck.size());
        blackCardInGame = blackDeck.get(randomCardPosition);
    }

    private void announceStartOfGame(Game game) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            for (ClientConnectionHandler client : game.players) {
                try {
                    client.writeMessage(Messages.GAME_STARTING + (10 - i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //Thread.sleep(1000);
        }
    }

    private void clearScreen(Game game) {
        for (ClientConnectionHandler player : game.players) {
            Server.announceInGame("\033[H\033[2J", game);
            System.out.flush();
        }
    }

    public void join(ClientConnectionHandler clientConnectionHandler) throws IOException {
        players.add(clientConnectionHandler);
        System.out.println(players);
        clientConnectionHandler.writeMessage(Messages.JOINED_GAME + this.name);
    }


    public List<String> initializeWhiteDeck() throws IOException {
        Path path = Paths.get("src/Decks/whiteDeck.txt");
        return Files.readAllLines(path);
    }

    public List<String> initializeBlackDeck() throws IOException {
        Path path = Paths.get("src/Decks/blackDeck.txt");
        return Files.readAllLines(path);
    }


    public synchronized boolean allPlayersVoted() {
        for (ClientConnectionHandler player : players) {
            if (player.getCorrespondingClient().isVoteState()) {
                return false;
            }
        }
        return true;
    }

    public void handleVotingResult() throws IOException {
        if (allPlayersVoted()) {
            announceVoteResult();
        }
    }

    public synchronized void clearPlayedCards() {
        roundCardsToVote.clear();
    }

    public synchronized List<String> getRoundCardsForPlayer(ClientConnectionHandler player) {
        return roundCardsToVote.stream()
                //    .filter(card -> !player.getCorrespondingClient().getCards().contains(card))
                .filter(card -> !card.equals(player.getCorrespondingClient().getPlayedCard()))
                .collect(Collectors.toList());
    }

    public void sortedPlayersByAge(){
        Collections.sort(players, (player1, player2) -> Integer.compare(player1.getPlayingGame().getClientAge(player1), player2.getPlayingGame().getClientAge(player2)));
    }

    public int getClientAge(ClientConnectionHandler client){
        return client.getCorrespondingClient().getAge();
    }
    public List<String> getWhiteDeck() {
        return whiteDeck;
    }

    public void setWhiteDeck(List<String> whiteDeck) {
        this.whiteDeck = whiteDeck;
    }

    public List<String> getBlackDeck() {
        return blackDeck;
    }

    public void setBlackDeck(List<String> blackDeck) {
        this.blackDeck = blackDeck;
    }

    public void setCardsInGame(String card) {
        cardsInGame.add(card);
    }

    public void setPlayedCardsCounter(int playedCardsCounter) {
        this.playedCardsCounter = playedCardsCounter;
    }

}