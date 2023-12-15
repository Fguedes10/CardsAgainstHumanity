package Client;

import java.io.IOException;
import java.net.Socket;


/**
 * Main function that starts the client connection to the server.
 *
 * @param  args  the command-line arguments
 * @throws IOException  if an I/O error occurs when creating the socket
 */
public class ClientLauncher {

    public static void main(String[] args) throws IOException {
        Client client = new Client("Client-" + Client.numberOfConnections++,0);
        Socket socket = new Socket(Client.SERVER_HOST, Client.SERVER_PORT);
        client.start(socket);
    }
}
