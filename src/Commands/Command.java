package Commands;

public enum Command {

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



    //After game starts:
    PLAY_CARD("/play_card", new PlayCardHandler()),//(Number of cards)
    FILL_HAND("/fill_hand", new FillHandHandler()),
    START_TURN("/start_turn", new TurnHandler()),




    //After game ends:
    NEW_GAME("/new_game", new NewGameHandler()),//(same clients)
    QUIT("/quit", new QuitHandler());


    private String description;
    private CommandHandler handler;




    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
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