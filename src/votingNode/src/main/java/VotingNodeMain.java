
// Required imports for Ice communication and voting functionality
import Contract.VotingSitePrx;
import comunication.VotingNodeImpl;
import controller.VotingNodeController;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VotingNodeMain {
  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
      // Create an adapter for the voting station(for the Alejandro's testing)
      ObjectAdapter adapter = communicator.createObjectAdapter("VoteStation");

      // Get the proxy to communicate with the VotingSite
      String proxyProperty = communicator.getProperties().getProperty("VotingSite.Proxy");

      VotingSitePrx votingSitePrx = VotingSitePrx.checkedCast(
          communicator.stringToProxy(proxyProperty));

      if (votingSitePrx == null) {
        System.err.println("No se pudo obtener el proxy de VotingSite.");
        return;
      }

      // Initialize the controller and node implementation
      VotingNodeController controller = new VotingNodeController(votingSitePrx);
      VotingNodeImpl node = new VotingNodeImpl(controller);

      // Add the node to the adapter with identity "VoteStation"
      adapter.add(node, Util.stringToIdentity("VoteStation"));

      // List of available candidates
      String[] candidatos = { "1. Juan Pérez", "2. Ana Gómez", "3. Luis Torres", "4. María Ruiz" };

      // Main program loop
      while (true) {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Votar");
        System.out.println("2. 🔥");
        int option = scanner.nextInt();
        scanner.nextLine(); // Clear scanner buffer

        if (option == 1) {
          // Normal voting process
          System.out.print("Ingrese su ID de votante: ");
          String voterId = scanner.nextLine();

          // Display candidate options
          System.out.println("Elija el número del candidato:");
          for (String candidato : candidatos) {
            System.out.println(candidato);
          }
          System.out.print("Número >> ");
          String opcionCandidato = scanner.nextLine();
          String candidateId = "";
          // Map numeric choice to candidate name
          switch (opcionCandidato) {
            case "1":
              candidateId = "001";
              break;
            case "2":
              candidateId = "002";
              break;
            case "3":
              candidateId = "003";
              break;
            case "4":
              candidateId = "004";
              break;
            default:
              System.out.println("Opción no válida. Intente de nuevo.");
              continue;
          }

          node.voteCLI(voterId, candidateId);

        } else if (option == 2) {
          // Stress test option - sends multiple votes
          // adapter.deactivate();
          System.out.print("Ingrese el número de votos a enviar: ");
          int numVotes = scanner.nextInt();
          scanner.nextLine(); // Clear scanner buffer
          System.out.print("Ingresa el segmento de votantes");
          int segment = scanner.nextInt();
          scanner.nextLine(); // Clear scanner buffer
          fire(node, numVotes, segment);
        } else {
          System.out.println("Opción no válida. Intente de nuevo.");
          continue;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    scanner.close();
  }

  // Helper method to simulate multiple votes for stress testing usando ThreadPool
  public static void fire(VotingNodeImpl node, int numVotes, int segment) {
    int threads = Math.min(numVotes, Runtime.getRuntime().availableProcessors() * 2);
    System.out.println("[INFO] Enviando " + numVotes + " votos usando " + threads + " hilos. Segmento: " + segment);

    // Preparar datos antes de medir el tiempo
    List<String> voterIds = loadVoterIds(segment, numVotes);
    String[] candidateIds = { "001", "002", "003", "004" };
    java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threads);

    long startTime = System.currentTimeMillis();

    for (int i = 0; i < numVotes; i++) {
      final int idx = i;
      executor.submit(() -> {
        String voterId = voterIds.get(idx % voterIds.size());
        String candidateId = candidateIds[idx % candidateIds.length];
        node.voteCLI(voterId, candidateId);
      });
    }
    executor.shutdown();
    try {
      executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    long endTime = System.currentTimeMillis();
    System.out.println("[INFO] Todos los votos enviados. Tiempo total: " + (endTime - startTime) + " ms");
  }

  // Carga un segmento específico del CSV de IDs de votantes
  public static List<String> loadVoterIds(int segment, int segmentSize) {
    InputStream inputStream = VotingNodeMain.class.getClassLoader().getResourceAsStream("ciudadano.csv");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    List<String> lines = reader.lines()
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .collect(Collectors.toList());

    int fromIndex = segment * segmentSize;
    int toIndex = Math.min(fromIndex + segmentSize, lines.size());
    if (fromIndex >= lines.size()) {
      throw new IllegalArgumentException("Segmento fuera de rango. El archivo solo tiene " + lines.size() + " líneas.");
    }
    return lines.subList(fromIndex, toIndex);
  }

}
