package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

/**
 * The `PlayCardHandler` class implements the `CommandHandler` interface and represents the handler for playing a card in the game.
 * It processes the player's input to play a card, updates the game state accordingly, and handles exceptions.
 */

public class PlayCardHandler implements CommandHandler {


    /**
     * Executes the command for playing a card. It validates the player's input and updates the game state accordingly.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     * @throws IOException If an I/O error occurs while communicating with the client.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String message = clientConnectionHandler.getMessage();

        try {
            Integer playedNumber = Integer.parseInt(message.split(" ")[1]);
            int indexToPlay = playedNumber - 1;

            List<String> playerCards = clientConnectionHandler.getCorrespondingClient().getCards();

            if (indexToPlay >= 0 && indexToPlay < playerCards.size()) {
                String playedCard = playerCards.get(indexToPlay);

                Server.announceInGame(clientConnectionHandler.getName() + Messages.PLAYER_HAS_PLAY, clientConnectionHandler.getPlayingGame());
                clientConnectionHandler.getPlayingGame().setCardsInGame(playedCard);
                clientConnectionHandler.getPlayingGame().cardsInGame.stream().sorted();
                clientConnectionHandler.getCorrespondingClient().setPlayedCard(playedCard);
                clientConnectionHandler.getPlayingGame().submitCard(playedCard, clientConnectionHandler);
                clientConnectionHandler.getPlayingGame().roundCardsToVote.add(playedCard);
                playerCards.remove(playedCard);
                clientConnectionHandler.getPlayingGame().incrementPlayedCardsCounter();
            } else {
                clientConnectionHandler.writeMessage(Messages.SELECT_A_VALID_CARD);
            }
        } catch (NumberFormatException e) {
            clientConnectionHandler.writeMessage(Messages.NOT_A_NUMBER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientConnectionHandler.getCorrespondingClient().setVoteState(true);
    }

}

