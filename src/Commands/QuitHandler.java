package Commands;

import Messages.Message;
import Server.Server;

public class QuitHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        server.removeClient(clientConnectionHandler);
        server.broadcast(clientConnectionHandler.getName(),
                clientConnectionHandler.getName() + Message.CLIENT_DISCONNECTED);
    }
}
