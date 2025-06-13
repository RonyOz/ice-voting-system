// Required imports for Ice communication and voting functionality
import Contract.VotingSitePrx;
import comunication.VotingNodeImpl;
import controller.VotingNodeController;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectAdapter;

import java.util.Scanner;

public class VotingNode {
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
            String[] candidatos = {"1. Juan Pérez", "2. Ana Gómez", "3. Luis Torres", "4. María Ruiz"};

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
                    System.out.print("Número: ");
                    String opcionCandidato = scanner.nextLine();
                    String candidateId = "";
                    // Map numeric choice to candidate name
                    switch (opcionCandidato) {
                        case "1": candidateId = "Juan Pérez"; break;
                        case "2": candidateId = "Ana Gómez"; break;
                        case "3": candidateId = "Luis Torres"; break;
                        case "4": candidateId = "María Ruiz"; break;
                        default:
                            System.out.println("Opción no válida. Se usará 'Juan Pérez' por defecto.");
                            candidateId = "Juan Pérez";
                    }
                    node.voteCLI(voterId, candidateId);
                } else if (option == 2) {
                    // Stress test option - sends multiple votes
                    adapter.deactivate();
                    System.out.print("Ingrese el ID del nodo: ");
                    String nodeId = scanner.nextLine();
                    System.out.print("Ingrese el número de votos a enviar: ");
                    int numVotes = scanner.nextInt();
                    scanner.nextLine(); // Clear scanner buffer
                    fire(node, nodeId, numVotes);
                } else {
                    System.out.println("Opción no válida. Intente de nuevo.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();        
    }

    // Helper method to simulate multiple votes for stress testing
    public static void fire(VotingNodeImpl node, String nodeId, int numVotes) {
        for (int i = 0; i < numVotes; i++) {
            node.voteCLI(nodeId + "_voterId" + i, nodeId + "_candidateId" + i);
        }
    }

}
