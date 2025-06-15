package impl;

import com.zeroc.Ice.Current;

import Contract.Candidate;
import Contract.ConsultService;
import controller.ConsultServiceController;

public class ConsultServiceImpl implements ConsultService {

  private ConsultServiceController consultServiceController;

  @Override
  public String getVotingLocation(String voterId, Current current) {
    return consultServiceController.getVotingLocation(voterId);
  }

  @Override
  public Candidate[] setCandidates(Current current) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setCandidates'");
  }

  @Override
  public Candidate[] getCandidates(Current current) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCandidates'");
  }

  @Override
  public String getResults(Current current) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getResults'");
  }

}
