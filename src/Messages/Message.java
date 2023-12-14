package Messages;

public abstract class Message {

    public final static String WELCOME = "Welcome to Cards Against Humanity";
    public final static String WINNER = " wins the game!";
    public final static String ROUND = "Round Nº";
    public final static String INPUT_NAME = "Input your username: ";
    public final static String NULL_NAME = "Write a valid username";
    public final static String REPEATED_NAME = "Username already taken, please choose another";
    public final static String INPUT_AGE = "Input your age: ";
    public final static String NULL_AGE = "Please insert a valid number";
    public final static String NOT_A_NUMBER = "That isn't a valid number";
    public final static String COMMANDS_LIST =
            "/start - > Start the game \n" + "/list_players -> List the names of online players \n" + "/change_name " +
                    "-> Change username \n" + "/whisper -> send private message to other player \n";
    public final static String PLAYER_SCORE = "Player points: ";

    public final static String PLAYER_ENTERED_CHAT = " joins the chat";

    public final static String CLIENT_DISCONNECTED = " has disconnected";


}