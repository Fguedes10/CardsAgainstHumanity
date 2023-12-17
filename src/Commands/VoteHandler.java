package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;

public class VoteHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;
        clientConnectionHandler.writeMessage("VOTING PHASE START");

        int index = 1;
        for (String card : owner.getPlayingGame().roundCardsToVote) {
            clientConnectionHandler.writeMessage(index + " - " + card);
            index++;
        }

        String voteCommand = clientConnectionHandler.getMessage();
        try {
            int votedCardIndex = Integer.parseInt(voteCommand.split(" ")[1]) - 1;

            if (votedCardIndex >= 0 && votedCardIndex < owner.getPlayingGame().roundCardsToVote.size()) {
                String votedCard = owner.getPlayingGame().roundCardsToVote.get(votedCardIndex);
                owner.getPlayingGame().announceVoteResult(votedCard);
            } else {
                clientConnectionHandler.writeMessage("INVALID VOTE");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            clientConnectionHandler.writeMessage("INVALID VOTE");
        }
    }
}
