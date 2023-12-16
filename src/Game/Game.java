package Game;

import Client.ClientConnectionHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Messages.Messages;
import Server.Server;
import Client.Client;

public class Game {

    public String name;
    public int MaxNumOfPlayers;
    public ArrayList<ClientConnectionHandler> players = new ArrayList<>();
    public ClientConnectionHandler owner;
    public boolean state = false;

    private String blackCardInGame;

    public String getBlackCardInGame() {
        return blackCardInGame;
    }

    private List<String> whiteDeck = initializeWhiteDeck();

    private List<String> blackDeck = initializeBlackDeck();

    public static List<Game> runningGames = new LinkedList<>();

    public boolean checkAllPlayersInGame(Game game){
        if(game.players.size() == game.MaxNumOfPlayers){
            game.state = true;
            return true;
        }
        return false;
    }

    public Game(ClientConnectionHandler owner, int maxNumOfPlayers, String name){
        this.owner = owner;
        this.name = name;
        this.MaxNumOfPlayers = maxNumOfPlayers;
        runningGames.add(this);
        players.add(this.owner);
        System.out.println(players);
    }

    public static String getRunningGames() throws IOException {
        StringBuffer buffer = new StringBuffer();
        runningGames.forEach(game -> buffer.append(game.name).append(" - ").append(game.MaxNumOfPlayers).append(" player game with ").append(game.MaxNumOfPlayers - game.players.size()).append(" free spots ").append("started by ").append(game.owner.getName()).append("\n"));
        return buffer.toString();
    }

    public static Game getGameByOwner(ClientConnectionHandler clientConnectionHandler) {
        for (int i = 0; i < runningGames.size(); i++) {
            if(runningGames.get(i).owner.toString().equalsIgnoreCase(clientConnectionHandler.getName())){
                return runningGames.get(i);
            }
        } return null;
    }

    public static void askForCard(Client client, String card){

    }

    public static Game getGameByName(String name) {
        for (int i = 0; i < runningGames.size(); i++) {
            if(runningGames.get(i).name.equalsIgnoreCase(name)){
                return runningGames.get(i);
            }
        } return null;
    }

    public void startGame(Game game) throws InterruptedException, IOException {
        announceStartOfGame(game);
        //Inserir lógica do jogo
        clearScreen(game);
        clearScreen(game);
        clearScreen(game);
        Server.announceInGame(Messages.GAME_BEGINS, game);
    }

    public List<String> initializeWhiteDeck(){
        String filePath = "src/Decks/whiteDeck.txt";
        List<String> whiteCardList = new ArrayList<>();

        // Use a try-with-resources statement to automatically close the BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file until the end is reached
            while ((line = br.readLine()) != null) {
                whiteCardList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return whiteCardList;
    }
    public List<String> initializeBlackDeck(){
        String filePath = "src/Decks/blackDeck.txt";
        List<String> blackCardList = new ArrayList<>();

        // Use a try-with-resources statement to automatically close the BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file until the end is reached
            while ((line = br.readLine()) != null) {
                blackCardList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blackCardList;
    }





    public static void announceStartOfGame(Game game) throws InterruptedException {
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

    public static void clearScreen(Game game){
        for (int i = 0; i < game.players.size(); i++) {
            Server.announceInGame("\033[H\033[2J", game);
            System.out.flush();
        }

    }

    public void join(ClientConnectionHandler clientConnectionHandler) throws IOException {
        players.add(clientConnectionHandler);
        System.out.println(players);
        clientConnectionHandler.writeMessage(Messages.JOINED_GAME + this.name);
    }

    public void presentBlackCard(){
            chooseBlackCard();
            Server.announceInGame("This turn's Black Card is: " + blackCardInGame, this);

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

    public void chooseBlackCard() {
        int randomCardPosition = (int) Math.random() * (blackDeck.size());
        blackCardInGame = blackDeck.get(randomCardPosition);
        //return correspondingClientConnectionHandlers.getPlayingGame().getWhiteDeck().get(randomCardPosition);
    }
}
