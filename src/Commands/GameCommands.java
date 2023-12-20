package Commands;

import Commands.Handlers.*;

/**
 * The `GameCommands` enum represents the various game-related commands along with their corresponding handlers.
 * Each enum constant includes a description and a handler to execute the associated command logic.
 */

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

    /**
     * Constructs a `GameCommands` enum constant with the specified description and command handler.
     *
     * @param description The description of the command.
     * @param handler     The handler for executing the command logic.
     */
    GameCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    /**
     * Retrieves the `GameCommands` enum constant based on the provided description.
     *
     * @param description The description of the command.
     * @return The corresponding `GameCommands` enum constant.
     */

    public static GameCommands getCommandFromDescription(String description) {
        for (GameCommands gameCommand : values()) {
            if (description.equals(gameCommand.description)) {
                return gameCommand;
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