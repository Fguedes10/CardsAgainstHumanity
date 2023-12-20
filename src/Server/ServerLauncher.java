package Server;


import java.io.IOException;

/**
 * The `ServerLauncher` class is responsible for launching the server for the GameName project.
 * It creates an instance of the `Server` class and starts it on a specified port.
 * If an IOException occurs during the server start process, the program prints an error message
 * and throws a RuntimeException to terminate the application.
 */
public class ServerLauncher {
    /**
     * Main method to launch the server for the GameName project.
     *
     * @param args Command-line arguments (not used in this application).
     */
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
