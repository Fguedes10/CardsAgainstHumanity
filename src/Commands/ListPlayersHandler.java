package Commands;

public class ListPlayersHandler implements CommandHandler {

        @Override
        public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        clientConnectionHandler.send(server.listClients());
        }

}
