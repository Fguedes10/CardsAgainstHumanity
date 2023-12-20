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
            List<String> cardsToVote = clientConnectionHandler.getPlayingGame().cardSubmissions.keySet().stream().toList();
            System.out.println(cardsToVote);
            if (votedCardIndex >= 0 && votedCardIndex <= cardsToVote.size()) {
                String votedCard = cardsToVote.get(votedCardIndex);
                clientConnectionHandler.getCorrespondingClient().playerVote = votedCard;
                ClientConnectionHandler votedClient = clientConnectionHandler.getPlayingGame().players.stream().filter(player -> player.getCorrespondingClient().getPlayedCard().equals(votedCard)).findFirst().get();
                System.out.println(votedClient.getName() + " one vote");
                votedClient.getCorrespondingClient().setRoundCardScore(votedClient.getCorrespondingClient().getRoundCardScore() + 1);
                System.out.println(votedClient.getCorrespondingClient().getRoundCardScore());
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
