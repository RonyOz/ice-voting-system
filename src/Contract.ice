module Contract { //Name of the folder where it will be compiled

    struct Vote {
        string voterId; // Unique identifier for the voter
        string candidateId; // Unique identifier for the candidate
    }

    interface VotingNode{
        void vote(string voterId, string candidateId);
    }

    interface VotingSite{
        void sendVote(Vote vote);
    }

    interface VotingService{

    }
    
}