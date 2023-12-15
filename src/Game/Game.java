package Game;

import Client.ClientConnectionHandler;

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

    public static List<Game> runningGames = new LinkedList<>();

    public boolean checkAllPlayersInGame(Game game){
        if(game.players.size() == game.MaxNumOfPlayers){
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

    public void join(ClientConnectionHandler clientConnectionHandler) throws IOException {
        players.add(clientConnectionHandler);
        System.out.println(players);
        clientConnectionHandler.writeMessage(Messages.JOINED_GAME + this.name);
    }
}
