package communication;

import com.zeroc.Ice.Current;

import Contract.Candidate;
import Contract.ConsultService;

public class ConsultServiceImpl implements ConsultService {

    public ConsultServiceImpl() {
    }

    @Override
    public String getVotingLocation(String voterId, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVotingLocation'");
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
