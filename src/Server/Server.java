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

public class Server {

    public static List<ClientConnectionHandler> clientHandlerList;


    void start(int port) throws IOException {
        clientHandlerList = new LinkedList<>();
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (serverSocket.isBound()) {
            System.out.println(Messages.SERVER_ON);
            Socket socket = serverSocket.accept();
            ClientConnectionHandler clientHandler = new ClientConnectionHandler(socket);
            clientHandlerList.add(clientHandler);
            executorService.submit(clientHandler);

        }

    }

    public static void broadcast(String name, String message){
        clientHandlerList.stream()
                .filter(handler -> handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }


    public Optional<ClientConnectionHandler> getClientByName(String name) {
        return clientHandlerList.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }
    public void removeClient(ClientConnectionHandler clientConnectionHandler){
        clientHandlerList.remove(clientConnectionHandler);
    }

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