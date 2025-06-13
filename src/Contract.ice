module Contract { //Name of the folder where it will be compiled

    struct Vote {
        string voterId; // Unique identifier for the voter
        string candidateId; // Unique identifier for the candidate
    }

    interface VotingNode{
        int vote(string voterId, string candidateId);
    }

    interface VotingSite{
        void sendVote(Vote vote);
    }

    // interface VotingService{ // This interface is not used in the current implementation because the definition is the RMDestination of reliable messaging

    // }

 
    
}