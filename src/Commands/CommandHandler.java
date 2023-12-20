package Commands;

import Client.ClientConnectionHandler;
import Server.Server;

import java.io.IOException;

/**
 * The `CommandHandler` interface outlines the contract for classes that handle commands in the server-client communication.
 * Implementing classes must provide the logic to execute a specific command based on the server and client connection handler.
 */
public interface CommandHandler {
    /**
     * Executes the command handling logic based on the provided server and client connection handler.
     *
     * @param server                  The server object to execute the command on.
     * @param clientConnectionHandler The client connection handler to process the command.
     * @throws IOException if an I/O error occurs during command execution.
     */
    void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException;
}
