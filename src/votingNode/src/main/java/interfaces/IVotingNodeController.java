package interfaces;

public interface IVotingNodeController {

    // el document y el candidateId son el voterId y el candidateId  de Vote.java de Contract
    void vote(String voterId, String candidateId);
    
}



