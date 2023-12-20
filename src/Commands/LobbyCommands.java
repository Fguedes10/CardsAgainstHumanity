package Commands;

import Commands.Handlers.*;

/**
 * The `LobbyCommands` enum represents various commands applicable before and after the game.
 * Each enum constant includes a description and a handler to execute the associated command logic.
 */
public enum LobbyCommands {

    //Before game starts:

    START("/start", new StartHandler()),
    BUILD("/build", new BuildHandler()),//(number of players)
    AVAILABLE_GAMES("/available_games", new ListGameHandler()),
    JOIN("/join", new JoinGameHandler()),
    LIST_PLAYERS("/list_players", new ListPlayersHandler()),
    CHANGE_NAME("/change_name", new ChangeNameHandler()),
    CHAT_GENERAL("/chat_general", new ChatGeneralHandler()),
    WHISPER("/whisper", new WhisperHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler()),//(lists of commands)


    //After game ends:
    NEW_GAME("/new_game", new NewGameHandler()),//(same clients)
    QUIT("/quit", new QuitHandler());


    private String description;
    private CommandHandler handler;

    /**
     * Constructs a `LobbyCommands` enum constant with the specified description and command handler.
     *
     * @param description The description of the command.
     * @param handler     The handler for executing the command logic.
     */
    LobbyCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    /**
     * Retrieves the `LobbyCommands` enum constant based on the provided description.
     *
     * @param description The description of the command.
     * @return The corresponding `LobbyCommands` enum constant.
     */

    public static LobbyCommands getCommandFromDescription(String description) {
        for (LobbyCommands lobbyCommands : values()) {
            if (description.equals(lobbyCommands.description)) {
                return lobbyCommands;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Retrieves the command handler associated with the enum constant.
     *
     * @return The command handler.
     */

    public CommandHandler getHandler() {
        return handler;
    }

    /**
     * Retrieves the description of the command.
     *
     * @return The description of the command.
     */
    public String getDescription() {
        return description;
    }


}