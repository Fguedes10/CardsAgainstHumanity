package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

public class PlayCardHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        //  ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;
        String message = clientConnectionHandler.getMessage();

        try {
            Integer playedNumber = Integer.parseInt(message.split(" ")[1]);
            int indexToPlay = playedNumber - 1;

            List<String> playerCards = clientConnectionHandler.getCorrespondingClient().getCards();

            if (indexToPlay >= 0 && indexToPlay < playerCards.size()) {
                String playedCard = playerCards.get(indexToPlay);

                clientConnectionHandler.send(clientConnectionHandler.getName() + Messages.PLAYER_HAS_PLAY);
                clientConnectionHandler.getPlayingGame().setCardsInGame(playedCard);
                clientConnectionHandler.getPlayingGame().roundCardsToVote.add(playedCard);
                clientConnectionHandler.getCorrespondingClient().setPlayedCard(playedCard);
                playerCards.remove(playedCard);

                clientConnectionHandler.getPlayingGame().incrementPlayedCardsCounter();

                if (clientConnectionHandler.getPlayingGame().allPlayersPlayedCards()) {
                    startVotingPhase(clientConnectionHandler);
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

    private void startVotingPhase(ClientConnectionHandler owner) throws IOException {
        Server.announceInGame(Messages.VOTING_PHASE_START, owner.getPlayingGame());

        int index = 1;

        for (ClientConnectionHandler player : owner.getPlayingGame().players) {
//            if (!player.equals(owner)) {
            player.writeMessage(Messages.VOTING_INSTRUCTIONS);
            List<String> cardsToVote = owner.getPlayingGame().getRoundCardsForPlayer(player);
            player.getCorrespondingClient().setVoteState(true);
            for (String card : cardsToVote) {
                player.writeMessage(index + " - " + card);
                index++;
            }

            index = 1;
            // }
        }

        Server.announceInGame(Messages.VOTING_PHASE_START, owner.getPlayingGame());
    }
}