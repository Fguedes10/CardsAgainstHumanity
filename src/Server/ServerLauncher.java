package Server;


import java.io.IOException;

public class ServerLauncher {
    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.start(8500);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
