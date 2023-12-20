package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Card;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  VoteHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String voteCommand = clientConnectionHandler.getMessage();
        clientConnectionHandler.getCorrespondingClient().setVoteState(false);
        try {
            int votedCardIndex = Integer.parseInt(voteCommand.split(" ")[1]) - 1;
             //= clientConnectionHandler.getPlayingGame().getRoundCardsForPlayer(clientConnectionHandler);
            List<String> cardsToVote = clientConnectionHandler.getPlayingGame().cardSubmissions.keySet().stream().toList();
            if (votedCardIndex >= 0 && votedCardIndex <= cardsToVote.size()) {
                String votedCard = cardsToVote.get(votedCardIndex);
                clientConnectionHandler.getCorrespondingClient().playerVote = votedCard;
                ClientConnectionHandler votedClient = clientConnectionHandler.getPlayingGame().getSubmittingPlayer(votedCard);
                votedClient.getCorrespondingClient().setRoundCardScore(votedClient.getCorrespondingClient().getRoundCardScore() + 1);
            } else {
                clientConnectionHandler.writeMessage(Messages.INVALID_VOTE);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            clientConnectionHandler.writeMessage(Messages.INVALID_VOTE);
        }
        if (clientConnectionHandler.getPlayingGame().allPlayersVoted()) {
            clientConnectionHandler.getPlayingGame().handleVotingResult();
        }
    }}
