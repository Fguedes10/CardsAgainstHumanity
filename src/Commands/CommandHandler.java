package Commands;

import Server.Server;

public interface CommandHandler {

   void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler);
}
