package controller;

import java.util.List;

import Contract.Vote;
import interfaces.IVotingServiceController;
import repository.JdbcVoteRepository;

public class VotingServiceController implements IVotingServiceController {

    private JdbcVoteRepository repository;

    public VotingServiceController(JdbcVoteRepository jdbcRepository) {
        this.repository = jdbcRepository;
    }

    @Override
    public void registerVotesFromSite(List<Vote> voteBatch) {
        // Verificar que los votos no esten repetidos
        if (!checkDuplicateVotes(voteBatch)) {

            // Guardar los votos en la base de datos H2
            repository.saveAll(voteBatch);
            System.out.println("[INFO] Votes registered successfully from site.");

        }
        
    }

    @Override
    public boolean checkDuplicateVotes(List<Vote> voteBatch) {
        boolean hasDuplicates = false;
        for (Vote vote : voteBatch) {
            if (repository.exists(vote.voterId, vote.candidateId)) {
                hasDuplicates = true;
                System.out.println("[WARNING] Duplicate vote detected for voter: " + vote.voterId + " and candidate: "
                        + vote.candidateId);
            }
        }
        return hasDuplicates;
    }

}
