package communication;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;

import Contract.Candidate;
import Contract.ConsultService;
import controller.ConsultServiceController;
import repository.VoteRepository;
import repository.DBConnection;

import java.util.List;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsultServiceImpl implements ConsultService {

  private ConsultServiceController consultServiceController;
  private VoteRepository voteRepository;

  public ConsultServiceImpl(Communicator communicator) {
    this.consultServiceController = new ConsultServiceController(communicator);
    try {
      this.voteRepository = new VoteRepository(DBConnection.getInstance(communicator).connect());
      System.out.println("[INFO] VoteRepository initialized successfully");
    } catch (SQLException e) {
      System.err.println("[ERROR] Failed to initialize VoteRepository: " + e.getMessage());
    }
  }

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
    System.out.println("[INFO] Getting voting results...");
    StringBuilder results = new StringBuilder();
    
    List<VoteRepository.CandidateVoteResult> voteResults = voteRepository.countVotesByCandidate();
    if (voteResults == null || voteResults.isEmpty()) {
      results.append("No hay candidatos ni votos registrados.\n");
      return results.toString();
    }

    results.append("\n=== RESULTADOS DE LA VOTACIÓN ===\n");
    results.append(String.format("%-15s %-25s %s\n", "ID", "Nombre", "Votos"));
    results.append("------------------------------------------------------\n");
    for (VoteRepository.CandidateVoteResult r : voteResults) {
      results.append(String.format("%-15s %-25s %d\n", r.candidateId, r.name, r.votes));
    }
    results.append("==============================\n");
    System.out.println(results.toString());
    return results.toString();
  }

  @Override
  public String getResumeCSV(Current current) {
    try {
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
      String filename = "voting_results_" + timestamp + ".csv";
      
      try (FileWriter writer = new FileWriter(filename)) {
        // Escribir encabezado
        writer.write("ID,Nombre,Votos\n");
        
        // Obtener y escribir resultados
        List<VoteRepository.CandidateVoteResult> results = voteRepository.countVotesByCandidate();
        for (VoteRepository.CandidateVoteResult r : results) {
          writer.write(String.format("%s,%s,%d\n", r.candidateId, r.name, r.votes));
        }
      }
      
      System.out.println("[INFO] CSV file created successfully: " + filename);
      return "CSV file created: " + filename;
    } catch (IOException e) {
      System.err.println("[ERROR] Failed to create CSV file: " + e.getMessage());
      return "Error creating CSV file: " + e.getMessage();
    }
  }

  @Override
  public String getPartialCSV(Current current) {
    return "Partial CSV data"; // Placeholder for actual implementation
  }

}
