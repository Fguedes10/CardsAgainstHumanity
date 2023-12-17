package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

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
                owner.getPlayingGame().roundCardsToVote.add(playedCard);
                playerCards.remove(playedCard);

                owner.getPlayingGame().incrementPlayedCardsCounter();

                if (owner.getPlayingGame().allPlayersPlayedCards()) {
                    startVotingPhase(owner);
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
        Server.announceInGame("VOTING PHASE START", owner.getPlayingGame());

        int index = 1;
        for (String card : owner.getPlayingGame().roundCardsToVote) {
            owner.send(index + " - " + card);
            index++;
        }

        for (ClientConnectionHandler player : owner.getPlayingGame().players) {
            player.getCorrespondingClient().setVoteState(true);
            player.writeMessage("VOTING INSTRUCTIONS");
        }

        Server.announceInGame("VOTE INSTRUCTION", owner.getPlayingGame());
    }
}

