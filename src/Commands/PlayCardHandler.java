package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

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
                owner.getPlayingGame().roundCardsToVote.add(playedCard);
                playerCards.remove(playedCard);

                System.out.println("Size of roundCardsToVote: " + owner.getPlayingGame().roundCardsToVote.size());
                System.out.println("numberOfInGamePlayers: " + owner.getPlayingGame().numberOfInGamePlayers);
                if(owner.getPlayingGame().roundCardsToVote.size() == owner.getPlayingGame().maxNumOfPlayers){
                    int index = 1;
                    for (String card : owner.getPlayingGame().roundCardsToVote) {
                        clientConnectionHandler.writeMessage(index + " - " + card);
                        index++;
                    }
                }
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