package Game;

import Client.ClientConnectionHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Game {
    public int numOfPlayers;
    public ClientConnectionHandler owner;

    public static List<Game> runningGames = new LinkedList<>();

    public Game(ClientConnectionHandler owner, int numOfPlayers){
        this.owner = owner;
        this.numOfPlayers = numOfPlayers;
    }

    public static String getRunningGames(ClientConnectionHandler client) throws IOException {
        StringBuffer buffer = new StringBuffer();
        runningGames.forEach(game -> buffer.append(game.numOfPlayers).append(" player game started by ").append(game.owner).append("\n"));
        return buffer.toString();
    }

    public void startGame(){

    }
}
