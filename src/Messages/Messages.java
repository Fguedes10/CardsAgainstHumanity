package Messages;

public abstract class Messages {
    public static final String RED_BOLD = "\033[1;31m";
    public static final String BLUE_BOLD = "\033[1;34m";

    public static final String RESET_COLOR = "\033[0m";



    public final static String WELCOME = BLUE_BOLD + "Welcome to Cards Against Humanity \n" + RESET_COLOR;
    public final static String SERVER_ON = "Server is on, waiting connections";
    public final static String PLAYER_ENTERED_CHAT = " joins the chat";
    public final static String WINNER = " wins the game!";
    public final static String CHOOSE_GAME_NAME = "Input game name:";
    public final static String GAME_BUILT = "New game built with ";
    public final static String GAME_STARTING = "The game will start in ";
    public final static String NOT_YOUR_GAME = "This game is not yours to start.";
    public final static String ROUND = "Round Nº";
    public final static String COMMANDS_LIST =
             " AVAILABLE COMMANDS\n\n " + RED_BOLD + "/build - > Build a new game \n" + "/available_games - > Check available games \n" + "/join - > join available games \n" + "/start - > Start the game when all players have joined \n" + "/list_players -> List the names of online players \n" + "/change_name " +
                    "-> Change username \n" + "/whisper -> send private message to other player \n" + RESET_COLOR;

    public final static String WHISPER_INSTRUCTIONS = "Invalid whisper use. Correct use: '/whisper <username> <message>";
    public final static String JOIN_INSTRUCTIONS = "Invalid join use. Correct use: '/join <GameName>'";
    public final static String START_INSTRUCTIONS = "Invalid start use. Correct use: '/start <GameName>'";
    public final static String JOINED_GAME = "Successfully joined game - ";
    public final static String GAME_READY = "Your game is ready to start!";
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
    public final static String LOBBY = "\n You are in chat lobby now \n";
    public final static String CLIENT_CONNECTED = "Client connected";
    public final static String WAITING_MESSAGE = "Waiting for ";
    public final static String CHOOSE_N_PLAYERS = "Please choose number of players";
    public final static String SERVER_MESSAGE_SENT = "Message sent to client";

    // GAME MESSAGES

    public final static String GAME_BEGINS = "Welcome to Cards Against Mindera \n" + "Here are the game rules: \n"
            + "Each player needs to run /fill_hand to pick 7 game cards. \n"
            + "After that, and at every new turn, the game will send out a Black Card. \n"
            + "A Black Card is a prompt, which you will answer using one of the White Cards in your hand. \n"
            + "Once everyone chooses their answer, you will be required to vote in your favourite (other than your own) \n"
            + "The player whose card gets the more votes, wins a point. \n"
            + "The game ends at 10 points, at which time a winner will be declared.";



}