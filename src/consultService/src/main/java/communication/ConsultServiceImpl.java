package communication;

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
  public void setCandidates(Candidate[] candidates, Current current) {
    consultServiceController.setCandidates(candidates);
  }

  @Override
  public Candidate[] getCandidates(Current current) {
    return consultServiceController.getCandidates();
  }

  @Override
  public String getResults(Current current) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getResults'");
  }

  @Override
  public String getResumeCSV(Current current) {
    return "Resume CSV data"; // Placeholder for actual implementation
  }

  @Override
  public String getPartialCSV(Current current) {
    return "Partial CSV data"; // Placeholder for actual implementation
  }

}
