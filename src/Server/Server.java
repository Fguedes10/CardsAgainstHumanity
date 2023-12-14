package Server;


import Commands.Command;
import Messages.Message;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class Server {

    private ServerSocket serverSocket;

    private ExecutorService executorService;

    static List<ClientConnectionHandler> clients;

    private static final int PORT = 8080;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
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
        clientConnectionHandler.send(Message.WELCOME.formatted(clientConnectionHandler.getName()));
        clientConnectionHandler.send(Message.COMMANDS_LIST);
        broadcast(clientConnectionHandler.getName(), Message.PLAYER_ENTERED_CHAT);
    }

    public void removeClient(ClientConnectionHandler clientConnectionHandler){
        clients.remove(clientConnectionHandler);
    }

    public void broadcast(String name, String message){
        clients.stream()
                .filter(handler -> handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }


    public class ClientConnectionHandler implements Runnable {

        private String name;
        private Integer age;
        private Socket clientSocket;
        private final BufferedWriter out;
        private final BufferedReader in;
        private String message;
        private boolean isInLobby;


        public ClientConnectionHandler(Socket clientSocket, String name, Integer age) throws IOException {
            this.clientSocket = clientSocket;
            this.name = name;
            this.age = age;
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
            writeMessage(Message.INPUT_NAME);
            name = in.readLine();
            if(name == null){
                writeMessage(Message.NULL_NAME);
                askClientName();
            }
            if(!checkUsedUsernames(name)){
                writeMessage(Message.REPEATED_NAME);
                askClientName();
            }
        }

        private void askClientAge() throws IOException {
            writeMessage(Message.INPUT_AGE);
            String answerAge = in.readLine();
            if(answerAge == null){
                writeMessage(Message.NULL_AGE);
                askClientAge();
            } else {
                try{
                    Integer i = Integer.parseInt(answerAge);

                } catch (NumberFormatException nfe){
                    writeMessage(Message.NOT_A_NUMBER);
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

        public String listClients() {
            StringBuffer buffer = new StringBuffer();
            clients.forEach(client -> buffer.append(client.getName()).append("\n"));
            return buffer.toString();
        }

        public String getName() {
            return name;
        }


        @Override
        public void run() {
            addClient(this);
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


    }

}
