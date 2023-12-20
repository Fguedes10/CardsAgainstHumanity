package Commands;

import Commands.Handlers.*;

public enum GameCommands {

    PLAY_CARD("/play_card", new PlayCardHandler()),
    FILL_HAND("/fill_hand", new FillHandHandler()),
    START_TURN("/start_turn", new TurnHandler()),
    VOTE("/vote", new VoteHandler()),
    VOTE_START("/vote_start", new VoteStartHandler()),
    SHOW_HAND("/show_hand", new ShowHandHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler());

    private String description;
    private CommandHandler handler;


    GameCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static GameCommands getCommandFromDescription(String description) {
        for (GameCommands gameCommand : values()) {
            if (description.equals(gameCommand.description)) {
                return gameCommand;
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