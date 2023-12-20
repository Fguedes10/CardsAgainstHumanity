package Client;

import Commands.GameCommands;
import Commands.LobbyCommands;
import Game.Game;
import Messages.Messages;
import Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import static Server.Server.clientHandlerList;

/**
 * The `ClientConnectionHandler` class represents a handler for client connections in a server.
 * It implements the `Runnable` interface to run in a separate thread.
 * This class manages communication with a connected client, including input and output streams,
 * client information (name, age), and associated game states.
 */
public class ClientConnectionHandler implements Runnable {

    private Socket socket;
    private Client correspondingClient;
    private final BufferedReader in;
    private final PrintWriter out;
    private String name = "";
    private Integer age = 0;
    private String messageFromClient;
    private Game ownedGame;
    private Game playingGame;

    private Server server;

    public boolean gameState = false;


    /**
     * Constructs a `ClientConnectionHandler` instance for a given socket connection.
     *
     * @param socket The socket representing the client connection.
     * @throws RuntimeException If an I/O error occurs while setting up input and output streams.
     */
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

    /**
     * Reads a message from the input stream.
     *
     * @return the message read from the input stream
     */
    private String readMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the clients.
     *
     * @param message the message to be sent
     */
    private void sendMessage(String message) {
        Server.sendClientsMessage(this, message);
    }

    /**
     * Writes a message to the output stream and prints a server message.
     *
     * @param message the message to be written
     * @throws IOException if an I/O error occurs
     */
    public void writeMessage(String message) throws IOException {
        out.println(message);
        System.out.println(Messages.SERVER_MESSAGE_SENT);
    }

    /**
     * Checks if the given username has been used by any connected client.
     *
     * @param username the username to be checked
     * @return true if the username has been used, false otherwise
     */
    public boolean checkUsedUserNames(String username) {
        for (ClientConnectionHandler client : clientHandlerList) {
            if (client.getName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Asks the user to input the number of players and returns the input as an integer.
     *
     * @return the number of players chosen by the user
     * @throws IOException if there is an error reading the user input
     */
    public int askNumberOfPlayers() throws IOException {
        writeMessage(Messages.CHOOSE_N_PLAYERS);
        return Integer.parseInt(readMessage());
    }

    /**
     * Asks the client for their username.
     *
     * @throws IOException if there is an error reading the input
     */
    private void askClientUserName() throws IOException {
        writeMessage(Messages.INPUT_NAME);
        String checkName = readMessage();
        if (checkName == null) {
            writeMessage(Messages.NULL_NAME);
            askClientUserName();
        }
        if (checkUsedUserNames(checkName)) {
            writeMessage(Messages.REPEATED_NAME);
            askClientUserName();
        }
        setName(checkName);
        correspondingClient.setName(checkName);
    }

    /**
     * Asks the client for their age.
     *
     * @throws IOException if there is an error reading the input
     */
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
            age = Integer.parseInt(answerAge);
        }
    }


    /**
     * Executes the start of the lobby, asks the client for their name and age.
     *
     * @return void
     * @Override
     */
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
                    continue;
                }
            }
            System.out.println(Messages.WAITING_MESSAGE + name);
            if (isCommand(messageFromClient)) {
                try {
                    dealWithCommand(messageFromClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!isCommand(messageFromClient) && !gameState) {
                sendMessage(name + ": " + messageFromClient);
            }

        }

    }

    /**
     * Checks if the given message is a command.
     *
     * @param message the message to check
     * @return true if the message starts with "/", false otherwise
     */
    private boolean isCommand(String message) {
        return message.startsWith("/");
    }

    /**
     * Deals with the command received from the user.
     *
     * @param message the command message
     * @throws IOException if there is an I/O error
     */
    private void dealWithCommand(String message) throws IOException {
        String description = message.split(" ")[0];
        LobbyCommands lobbyCommand = LobbyCommands.getCommandFromDescription(description);
        lobbyCommand.getHandler().execute(this.server, this);
    }


    /**
     * Handles the game command specified in the given message.
     *
     * @param message the message containing the game command
     * @throws IOException if an I/O error occurs while executing the game command
     */
    private void dealWithGameCommand(String message) throws IOException {
        String description = message.split(" ")[0];
        GameCommands gameCommand = GameCommands.getCommandFromDescription(description);
        gameCommand.getHandler().execute(this.server, this);
    }

    /**
     * Sends a message.
     *
     * @param message the message to be sent
     */
    public void send(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * Retrieves a list of clients.
     *
     * @return a string representation of the list of client names
     */
    public String listClients() {
        StringBuffer buffer = new StringBuffer();
        Server.clientHandlerList.forEach(client -> buffer.append(client.getName()).append("\n"));
        return buffer.toString();
    }

    /**
     * Retrieves the message from the client.
     *
     * @return The message from the client.
     */
    public String getMessage() {
        return messageFromClient;
    }

    /**
     * Returns the name of the object.
     *
     * @return the name of the object
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the object.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Asks the user to enter the name of the game.
     *
     * @return the name of the game entered by the user
     * @throws IOException if an I/O error occurs
     */
    public String askNameOfGame() throws IOException {
        writeMessage(Messages.CHOOSE_GAME_NAME);
        return readMessage();
    }

    /**
     * Retrieves the corresponding client.
     *
     * @return the corresponding client
     */
    public Client getCorrespondingClient() {
        return correspondingClient;
    }

    /**
     * Sets the corresponding client for this object.
     *
     * @param correspondingClient the client to be set
     */
    public void setCorrespondingClient(Client correspondingClient) {
        this.correspondingClient = correspondingClient;
    }

    /**
     * Retrieves the currently playing game.
     *
     * @return the currently playing game
     */
    public Game getPlayingGame() {
        return playingGame;
    }

    /**
     * Sets the playing game.
     *
     * @param playingGame the game to set
     */
    public void setPlayingGame(Game playingGame) {
        this.playingGame = playingGame;
    }

    /**
     * Sets the game state.
     *
     * @param b the new game state
     */
    private void setGameState(Boolean b) {
        this.gameState = b;
    }

    /**
     * Sets the server for the class.
     *
     * @param server the server to set
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Sets the owned game for the player.
     *
     * @param ownedGame the game to set as owned
     */
    public void setOwnedGame(Game ownedGame) {
        this.ownedGame = ownedGame;
    }


}
