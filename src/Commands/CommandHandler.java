package Commands;

public interface CommandHandler {

    public interface CommandHandler {
        void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler);
    }
}
