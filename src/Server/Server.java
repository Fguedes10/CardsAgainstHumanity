package Server;


import Client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
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
            writeMessage("Input your username: ");
            name = in.readLine();
            if(name == null){
                writeMessage("Write a valid username");
                askClientName();
            }
            if(!checkUsedUsernames(name)){
                writeMessage("Username already taken, please choose another");
                askClientName();
            }
        }

        private void askClientAge() throws IOException {
            writeMessage("Input your age: ");
            String answerAge = in.readLine();
            if(answerAge == null){
                writeMessage("Please insert a valid number");
                askClientAge();
            }
            try{
                Integer i = Integer.parseInt(answerAge);

            } catch (NumberFormatException nfe){
                writeMessage("That isn't a valid number");
                askClientAge();
            }
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
        }

        private void checkForCommands(String messageFromClient) {
            
        }


    }

}
