package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;

public class ChangeNameHandler implements CommandHandler {

    /**
     * Executes the function.
     *
     * @param  server                 the server object
     * @param  clientConnectionHandler the client connection handler object
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        String name = clientConnectionHandler.getMessage().replace(Command.CHANGE_NAME.getDescription(), "").trim();
        if (server.getClientByName(name).isPresent()){
            clientConnectionHandler.send(Messages.REPEATED_NAME);
            return;
        }
        clientConnectionHandler.setName(name);
        clientConnectionHandler.send(Messages.ACCEPT_NEW_NAME + name);
    }
}
