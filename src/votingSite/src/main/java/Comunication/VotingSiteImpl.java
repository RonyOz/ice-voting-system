package Comunication;

import java.util.List;

import com.zeroc.Ice.Current;

import Contract.VotingSite;
import Controller.VotingSiteController;
import model.Message;
import reliableMessage.RMSourcePrx;
import Contract.Vote;

public class VotingSiteImpl implements VotingSite {

  private VotingSiteController controller;
  private RMSourcePrx rm;

  public VotingSiteImpl(VotingSiteController controller, RMSourcePrx rm) {
    this.controller = controller;
    this.rm = rm;
  }

  public void reportVoteBatch(List<Vote> voteBatch) {
    Message message = new Message(voteBatch);

    rm.sendMessage(message);
  }

  @Override
  public void sendVote(Vote vote, String nodeVoteID, Current current) {
    controller.processVote(vote, nodeVoteID);
  }

}
