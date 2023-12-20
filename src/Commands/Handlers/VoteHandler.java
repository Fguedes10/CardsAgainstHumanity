package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.List;

public class  VoteHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        if (clientConnectionHandler.getCorrespondingClient().isVoteState()) {
            clientConnectionHandler.writeMessage(Messages.VOTING_INSTRUCTIONS);

        }

        int index = 1;
        for (ClientConnectionHandler player : clientConnectionHandler.getPlayingGame().players) {
            if (!player.equals(clientConnectionHandler)) {
                List<String> cards = clientConnectionHandler.getPlayingGame().getRoundCardsForPlayer(player);
                for (String card : cards) {
                    clientConnectionHandler.writeMessage(index + " - " + card);
                    index++;
                }
            }
        }

        String voteCommand = clientConnectionHandler.getMessage();
        clientConnectionHandler.getCorrespondingClient().setVoteState(false);
        try {
            int votedCardIndex = Integer.parseInt(voteCommand.split(" ")[1]) - 1;

            List<String> cardsToVote = clientConnectionHandler.getPlayingGame().getRoundCardsForPlayer(clientConnectionHandler);

            if (votedCardIndex >= 0 && votedCardIndex < cardsToVote.size()) {
                String votedCard = cardsToVote.get(votedCardIndex);
                clientConnectionHandler.getCorrespondingClient().playerVote = votedCard;

                if (clientConnectionHandler.getPlayingGame().allPlayersVoted()) {
                    clientConnectionHandler.getPlayingGame().handleVotingResult();
                }
            } else {
                clientConnectionHandler.writeMessage(Messages.INVALID_VOTE);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            clientConnectionHandler.writeMessage(Messages.INVALID_VOTE);
        }
    }
}
