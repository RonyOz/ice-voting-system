package controller;

import java.util.List;

import Contract.Vote;
import interfaces.IVotingServiceController;
import repository.VoteRepository;

public class VotingServiceController implements IVotingServiceController {

    private VoteRepository repository;

    public VotingServiceController(VoteRepository jdbcRepository) {
        this.repository = jdbcRepository;
    }

    @Override
    public void registerVotesFromSite(List<Vote> voteBatch) {

        // Guardar los votos en la base de datos H2
        repository.saveAll(voteBatch);
        System.out.println("[INFO] Vote Batch registered successfully");

    }

}
