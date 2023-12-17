package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class PlayCardHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;

        String message = clientConnectionHandler.getMessage();

        try {
            Integer playedNumber = Integer.parseInt(message.split(" ")[1]);
            int indexToPlay = playedNumber - 1;

            List<String> playerCards = clientConnectionHandler.getCorrespondingClient().getCards();

            if (indexToPlay >= 0 && indexToPlay < playerCards.size()) {
                String playedCard = playerCards.get(indexToPlay);

                owner.send(clientConnectionHandler.getName() + Messages.PLAYER_HAS_PLAY);

                clientConnectionHandler.getPlayingGame().setCardsInGame(playedCard);

                System.out.println(clientConnectionHandler.getPlayingGame().cardsInGame);

                playerCards.remove(playedCard);
            } else {
                clientConnectionHandler.writeMessage(Messages.SELECT_A_VALID_CARD);
            }
        } catch (NumberFormatException e) {
            clientConnectionHandler.writeMessage(Messages.NOT_A_NUMBER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}