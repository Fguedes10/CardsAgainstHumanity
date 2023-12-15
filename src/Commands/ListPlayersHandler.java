package Commands;

import Client.ClientConnectionHandler;
import Server.Server;

import java.io.IOException;

public class ListPlayersHandler implements CommandHandler {


        @Override
        public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
                clientConnectionHandler.writeMessage(clientConnectionHandler.listClients());
        }

}
