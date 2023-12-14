package Server;


import Commands.Command;
import Messages.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server {

    private ServerSocket serverSocket;

    private ExecutorService executorService;

    static List<ClientConnectionHandler> clients;

    private static final int PORT = 8080;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void broadcast(String name, String message){
        clients.stream()
                .filter(handler -> handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    public void start(int port) throws IOException{
        clients = new LinkedList<>();
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            System.out.println("Listening to Connections");
            Socket socket = serverSocket.accept();
            ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(socket);
            addClient(clientConnectionHandler);
            executorService.submit(clientConnectionHandler);
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket clientSocket = serverSocket.accept();

        BufferedReader inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outClient = new PrintWriter(clientSocket.getOutputStream(), true);
        serverSocket.close();
    }

    private void addClient(ClientConnectionHandler clientConnectionHandler) {
        clients.add(clientConnectionHandler);
        clientConnectionHandler.send(Messages.WELCOME.formatted(clientConnectionHandler.getName()));
        clientConnectionHandler.send(Messages.COMMANDS_LIST);
        broadcast(clientConnectionHandler.getName(), Messages.PLAYER_ENTERED_CHAT);
    }

    public Optional<ClientConnectionHandler> getClientByName(String name) {
        return clients.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }


    public class ClientConnectionHandler implements Runnable {

        private String name;
        private Integer age;
        private Socket clientSocket;
        private final BufferedWriter out;
        private final BufferedReader in;
        private String message;
        private boolean isInLobby;

        public void startGame(){

        }


        public ClientConnectionHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.name = null;
            this.age = 0;
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.isInLobby = true;
        }

        public void writeMessage(String message) throws IOException {
            out.write(message);
        }
        public String readMessage(){
            try{
             return in.readLine();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        

        public boolean checkUsedUsernames(String username){
            Set<String> usernameList = clients.stream().map(clientConnectionHandler -> clientConnectionHandler.name).collect(Collectors.toSet());
            if(usernameList.size() < clients.size()){
                return false;
            }
            return true;
        }

        private void askClientName() throws IOException {
            writeMessage(Messages.INPUT_NAME);
            name = in.readLine();
            if(name == null){
                writeMessage(Messages.NULL_NAME);
                askClientName();
            }
            if(!checkUsedUsernames(name)){
                writeMessage(Messages.REPEATED_NAME);
                askClientName();
            }
        }

        private void askClientAge() throws IOException {
            writeMessage(Messages.INPUT_AGE);
            String answerAge = in.readLine();
            if(answerAge == null){
                writeMessage(Messages.NULL_AGE);
                askClientAge();
            } else {
                try{
                    Integer i = Integer.parseInt(answerAge);

                } catch (NumberFormatException nfe){
                    writeMessage(Messages.NOT_A_NUMBER);
                    askClientAge();
                }
            }

        }

        public void broadcast(String name, String message){
            clients.stream()
                    .filter(handler -> handler.getName().equals(name))
                    .forEach(handler -> handler.send(name + ": " + message));
        }

        public void send(String message){
            synchronized (out){
                try {
                    out.write(message);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        public String getName() {
            return name;
        }


        @Override
        public void run() {
            try {
                askClientName();
                askClientAge();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while(clientSocket.isConnected() && isInLobby){
                String messageFromClient;
                messageFromClient = readMessage();
                if(messageFromClient.startsWith("/")){
                    checkForCommands(messageFromClient);
                    continue;
                }
                sendMessage(name + " : " + messageFromClient);
            }
        }

        private void sendMessage(String s) {
            sendClientsMessage(this, message);
        }

        private void sendClientsMessage(ClientConnectionHandler sender, String message){
            clients.stream()
                    .filter(handler -> !handler.equals(sender))
                    .forEach(handler -> {
                try {
                    handler.writeMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void checkForCommands(String messageFromClient) {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);
            command.getHandler().execute(Server.this, this);
        }


        public String getMessage() {
            return message;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
