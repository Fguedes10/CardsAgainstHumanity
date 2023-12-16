package Client;

import Commands.Command;
import Game.Game;
import Messages.Messages;
import Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.stream.Collectors;


import static Server.Server.clientHandlerList;

public class ClientConnectionHandler implements Runnable {

    private Socket socket;
    private Client correspondingClient;
    private final BufferedReader in;
    private final PrintWriter out;
    private String name;
    private String messageFromClient;
    private Game ownedGame;
    private Game playingGame;


    private Server server;

    public boolean gameState = false;

    public Game getPlayingGame() {
        return playingGame;
    }

    public void setPlayingGame(Game playingGame) {
        this.playingGame = playingGame;
    }

    private void setGameState(Boolean b) {
        this.gameState = b;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOwnedGame(Game ownedGame) {
        this.ownedGame = ownedGame;
    }

    public ClientConnectionHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.correspondingClient = new Client();
        correspondingClient.setCorrespondingClientConnectionHandler(this);
    }


    private String readMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String message) {
        Server.sendClientsMessage(this, message);
    }

    public void writeMessage(String message) throws IOException {
        out.println(message);
        System.out.println(Messages.SERVER_MESSAGE_SENT);
    }

    public boolean checkUsedUserNames(String username) {
        Set<String> usernameList = clientHandlerList.stream().map(clientHandler -> clientHandler.name).collect(Collectors.toSet());
        if (usernameList.size() < clientHandlerList.size()) {
            return false;
        }
        return true;
    }

    public int askNumberOfPlayers() throws IOException {
        writeMessage(Messages.CHOOSE_N_PLAYERS);
        return Integer.parseInt(readMessage());
    }

    private void askClientUserName() throws IOException {
        writeMessage(Messages.INPUT_NAME);
        name = readMessage();
        if (name == null) {
            writeMessage(Messages.NULL_NAME);
            askClientUserName();
        }
        if (!checkUsedUserNames(name)) {
            writeMessage(Messages.REPEATED_NAME);
            askClientUserName();
        }
    }

    private void askClientAge() throws IOException {
        writeMessage(Messages.INPUT_AGE);
        String answerAge = in.readLine();
        if (answerAge == null) {
            writeMessage(Messages.NULL_AGE);
            askClientAge();
        } else {
            try {
                Integer i = Integer.parseInt(answerAge);

            } catch (NumberFormatException nfe) {
                writeMessage(Messages.NOT_A_NUMBER);
                askClientAge();
            }
        }

    }


    @Override
    public void run() {
        System.out.println(Messages.CLIENT_CONNECTED);
        out.println(Messages.WELCOME);

        try {
            askClientUserName();
            askClientAge();
            out.println(Messages.LOBBY);
            out.println(Messages.COMMANDS_LIST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (socket.isConnected()) {
            messageFromClient = readMessage();//blocking
            if (this.gameState) {
                if (isCommand(messageFromClient)) {
                    try {
                        dealWithGameCommand(messageFromClient);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Aqui, os jogadores que não começam o jogo ainda conseguem enviar uma
                    // mensagem porque o servidor já está à espera dalguma mensagem deles.
                    // É preciso descobrir como impedir isso.
                    return;
                }
            }
            System.out.println(Messages.WAITING_MESSAGE + name);
            if (isCommand(messageFromClient)) {
                try {
                    dealWithCommand(messageFromClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            sendMessage(name + ": " + messageFromClient);
        }

    }


    private boolean isCommand(String message) {
        return message.startsWith("/");
    }

    private void dealWithCommand(String message) throws IOException {
        String description = message.split(" ")[0];
        Command command = Command.getCommandFromDescription(description);
        command.getHandler().execute(this.server, this);
    }

    private void dealWithGameCommand(String message) throws IOException {
        String description = message.split(" ")[0];
        Command command = Command.getCommandFromDescription(description);
        command.getHandler().execute(this.server, this);
    }

    public void send(String message) {
        out.println(message);
        out.flush();
    }

    public String listClients() {
        StringBuffer buffer = new StringBuffer();
        Server.clientHandlerList.forEach(client -> buffer.append(client.getName()).append("\n"));
        return buffer.toString();
    }

    public String getMessage() {
        return messageFromClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String askNameOfGame() throws IOException {
        writeMessage(Messages.CHOOSE_GAME_NAME);
        return readMessage();
    }

    public Client getCorrespondingClient() {
        return correspondingClient;
    }

    public void setCorrespondingClient(Client correspondingClient) {
        this.correspondingClient = correspondingClient;
    }


}
