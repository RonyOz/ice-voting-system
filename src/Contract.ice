module Contract { //Name of the folder where it will be compiled

    struct Vote {
        string voterId; // Unique identifier for the voter
        string candidateId; // Unique identifier for the candidate
    }

    struct Candidate {
        string candidateId;
        string name;
    }

    sequence<Candidate> CandidateSeq;

    interface VotingNode{
        int vote(string voterId, string candidateId);
    }

    interface VotingSite{
        void sendVote(Vote vote,string nodeVoteID);
    }

    interface AuthService {
        int authenticate(string voterId, string mesaId);
    }

    interface ConsultService {
        string getVotingLocation(string voterId);
        void setCandidates(CandidateSeq candidates);
        CandidateSeq getCandidates();
        string getResults();
        string getResumeCSV();
        string getPartialCSV();
    }
    
    interface ControlCommand {
        void receiveCommand(string command);
    };
    
}
