package Messages;

public abstract class Messages {

    public final static String WELCOME = "Welcome to Cards Against Humanity";
    public final static String PLAYER_ENTERED_CHAT = " joins the chat";
    public final static String WINNER = " wins the game!";
    public final static String GAME_BUILT = "New game built with ";
    public final static String ROUND = "Round Nº";
    public final static String COMMANDS_LIST =
            "/build - > Build a new game \n" + "/available_games - > Check available games \n" + "/start - > Start the game \n" + "/list_players -> List the names of online players \n" + "/change_name " +
                    "-> Change username \n" + "/whisper -> send private message to other player \n";

    public final static String WHISPER_INSTRUCTIONS = "Invalid whisper use. Correct use: '/whisper <username> <message>";
    public final static String INPUT_NAME = "Input your username: ";
    public final static String NULL_NAME = "Write a valid username";
    public final static String REPEATED_NAME = "Username already taken, please choose another";
    public final static String NO_SUCH_CLIENT = "The client you want to whisper to doesn't exists.";
    public final static String WHISPER = "(whisper)";
    public final static String NO_SUCH_COMMAND = "⚠️ Invalid command!";
    public final static String ACCEPT_NEW_NAME = "Your new name is: ";
    public final static String INPUT_AGE = "What is your age: ";
    public final static String NULL_AGE = "Please insert a valid number";
    public final static String NOT_A_NUMBER = "That isn't a valid number";
    public final static String PLAYER_SCORE = "Player points: ";
    public final static String CLIENT_DISCONNECTED = " has disconnected";

}