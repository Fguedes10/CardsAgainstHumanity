package Commands;

public class ChangeNameHandler implements CommandHandler {

        @Override
        public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
            String name = clientConnectionHandler.getMessage()
                    .replace(Command.CHANGE_NAME.getDescription(), "").trim();
            clientConnectionHandler.setName(name);
            clientConnectionHandler.send("Your name has been changed to " + name);
        }
}
