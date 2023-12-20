package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Card;
import Messages.Messages;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The `VoteHandler` class implements the `CommandHandler` interface and represents the handler for processing player votes
 * during the voting phase of the game.
 * It allows players to cast their votes for the submitted cards and updates the scores accordingly.
 */
public class VoteHandler implements CommandHandler {

    /**
     * Executes the command for processing player votes during the voting phase.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     * @throws IOException If an I/O error occurs during vote processing or handling of the voting results.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String voteCommand = clientConnectionHandler.getMessage();
        clientConnectionHandler.getCorrespondingClient().setVoteState(false);
        try {
            int votedCardIndex = Integer.parseInt(voteCommand.split(" ")[1]) - 1;
            List<String> cardsToVote = clientConnectionHandler.getPlayingGame().cardSubmissions.keySet().stream().toList();
            if (votedCardIndex >= 0 && votedCardIndex <= cardsToVote.size()) {
                String votedCard = cardsToVote.get(votedCardIndex);
                clientConnectionHandler.getCorrespondingClient().playerVote = votedCard;
                ClientConnectionHandler votedClient = clientConnectionHandler.getPlayingGame().players.stream().filter(player -> player.getCorrespondingClient().getPlayedCard().equals(votedCard)).findFirst().get();
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
    }
}
