package Client;

import java.io.IOException;
import java.net.Socket;

/**
 * The `ClientLauncher` class is the entry point for launching the client application.
 * It creates an instance of the `Client` class and establishes a socket connection to the server.
 * The `main` method initiates the client by invoking the `start` method with the created socket.
 */
public class ClientLauncher {

    /**
     * The main method for launching the client application.
     *
     * @param args Command-line arguments (not used in this implementation).
     * @throws IOException If an I/O error occurs while setting up the client or establishing a socket connection.
     */
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Socket socket = new Socket(Client.SERVER_HOST, Client.SERVER_PORT);
        client.start(socket);

    }
}


