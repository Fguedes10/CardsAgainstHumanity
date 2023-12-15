package Server;

import Messages.Messages;

import java.io.*;
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
    public static final String ANSI_RED = "\u001B[31m";

    public static final String RED_BOLD = "\033[1;31m";

    public static final String YELLOW_UNDERLINED = "\033[4;33m";

   public List<ClientConnectionHandler> clientHandlerList;

    void start(int port) throws IOException {
        clientHandlerList = new LinkedList<>();
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            System.out.println("Listening to connections");
            Socket socket = serverSocket.accept();
            ClientConnectionHandler clientHandler = new ClientConnectionHandler(socket);
            clientHandlerList.add(clientHandler);
            executorService.submit(clientHandler);

        }

    }

   public void broadcast(String name, String message){
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

    private void sendClientsMessage(ClientConnectionHandler sender, String message) {
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

    private void sendWhisper(String sender, String receiver, String message){
        clientHandlerList.stream().filter(clientHandler -> clientHandler.name
                .equals(receiver)).forEach(clientHandler -> {
                    try {
                        clientHandler.writeMessage("Private message from " + sender + ": " + message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

   public class ClientConnectionHandler implements Runnable {
        private Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;
        private String name;
        private String message;

        public ClientConnectionHandler(Socket socket) {

            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String readMessage() {
            try {
                return in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void sendMessage(String message) {
            sendClientsMessage(this, message);
        }

        private void writeMessage(String message) throws IOException {
            out.println(message);
            System.out.println("Message sent to client");
        }

        public boolean checkUsedUserNames(String username){
            Set<String> usernameList = clientHandlerList.stream().map(clientHandler -> clientHandler.name).collect(Collectors.toSet());
            if(usernameList.size() < clientHandlerList.size()){
                return false;
            }
            return true;
        }

        private void askClientUserName() throws IOException {
            writeMessage("What is your username?");
            name = readMessage();
            if (name == null) {
                writeMessage("Please input a valid username.");
                askClientUserName();
            }
            if(!checkUsedUserNames(name)){
                writeMessage("Username already taken. Please choose another.");
                askClientUserName();
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

        @Override
        public void run() {
            System.out.println("Client Arrived");
            String messageFromClient;
            try {
                askClientUserName();
                askClientAge();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (socket.isConnected()) {
                System.out.println("Waiting for " + name + "!");
                messageFromClient = readMessage();//blocking
                if(messageFromClient.startsWith("/")){
                    try {
                        checkForSettings(messageFromClient);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                System.out.println("Message from " + name + "!");
                sendMessage(name + ": " + messageFromClient);
            }

        }

        private void checkForSettings(String messageFromClient) throws IOException {

            if(messageFromClient.startsWith("/users")){
                out.println("Online users: ");
                listClients();
            }
            if(messageFromClient.startsWith("/whisper")){
                String userToWhisper = messageFromClient.split(" ")[1];
                out.println("Type private message to send to " + userToWhisper);
                String privateMessage = in.readLine();
                sendWhisper(this.name, userToWhisper, privateMessage);

            }
            if(messageFromClient.equals("/")){
                out.println(RED_BOLD + "/users - list all users\n" + "/whisper + (username) - send private message\n");
            }
            if(messageFromClient.equalsIgnoreCase("Close")){
                out.println("Goodbye");
                in.close();
                out.close();
                socket.close();
            }
        }

       public void send(String message){
                out.println(message);
                   out.flush();
       }





       public String listClients(){
           StringBuffer buffer = new StringBuffer();
           clientHandlerList.forEach(client -> buffer.append(client.getName()).append("\n"));
           return buffer.toString();
       }
       public String getMessage() {
           return message;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

   }

}