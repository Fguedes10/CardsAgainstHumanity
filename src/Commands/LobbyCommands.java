package Commands;

import Commands.Handlers.*;

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


    LobbyCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static LobbyCommands getCommandFromDescription(String description) {
        for (LobbyCommands lobbyCommands : values()) {
            if (description.equals(lobbyCommands.description)) {
                return lobbyCommands;
            }
        }
        return NOT_FOUND;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public String getDescription() {
        return description;
    }


    }