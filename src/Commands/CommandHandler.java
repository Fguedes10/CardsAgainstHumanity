package Commands;

import Client.ClientConnectionHandler;
import Server.Server;

import java.io.IOException;

public interface CommandHandler {

   void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException;
}
