package Server;

import Client.Client;
import Client.ClientConnectionHandler;
import Game.Game;
import Messages.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The `Server` class represents the main server for the Cards Against Mindera game.
 * It handles client connections, broadcasts messages, and manages the list of connected clients.
 */

public class Server {

    /**
     * The list of client connection handlers representing connected clients.
     */
    public static List<ClientConnectionHandler> clientHandlerList;

    /**
     * Starts the server on the specified port, accepts client connections, and handles them using
     * a thread pool of client connection handlers.
     *
     * @param port the port on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs while setting up the server
     */
    void start(int port) throws IOException {
        clientHandlerList = new LinkedList<>();
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println(Messages.SERVER_ON);
        while (serverSocket.isBound()) {
            Socket socket = serverSocket.accept();
            ClientConnectionHandler clientHandler = new ClientConnectionHandler(socket);
            clientHandler.setServer(this);
            clientHandlerList.add(clientHandler);
            executorService.submit(clientHandler);

        }

    }

    /**
     * Broadcasts a message to a specific client with the given name.
     *
     * @param name    the name of the client to receive the message
     * @param message the message to broadcast
     */
    public static void broadcast(String name, String message) {
        clientHandlerList.stream()
                .filter(handler -> handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    /**
     * Announces a message to all players in a specific game.
     *
     * @param message the message to announce
     * @param game    the game in which the announcement is made
     */
    public static void announceInGame(String message, Game game) {
        game.players.forEach(handler -> handler.send(message));
    }


    /**
     * Retrieves a client connection handler by name.
     *
     * @param name the name of the client to retrieve
     * @return an optional containing the client connection handler if found, otherwise empty
     */
    public Optional<ClientConnectionHandler> getClientByName(String name) {
        return clientHandlerList.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Removes a client connection handler from the list of connected clients.
     *
     * @param clientConnectionHandler the client connection handler to remove
     */
    public void removeClient(ClientConnectionHandler clientConnectionHandler) {
        clientHandlerList.remove(clientConnectionHandler);
    }

    /**
     * Sends a message to all connected clients except the sender.
     *
     * @param sender  the client connection handler sending the message
     * @param message the message to send
     */

    public static void sendClientsMessage(ClientConnectionHandler sender, String message) {
        clientHandlerList.stream().filter(clientHandler -> !clientHandler.equals(sender)).forEach(
                clientHandler -> {
                    try {
                        clientHandler.writeMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

}