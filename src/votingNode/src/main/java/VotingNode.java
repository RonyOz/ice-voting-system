import Contract.VotingSitePrx;
import comunication.VotingNodeImpl;
import controller.VotingNodeController;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import java.util.Scanner;

public class VotingNode {
    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            String proxyProperty = communicator.getProperties().getProperty("VotingSite.Proxy");
            VotingSitePrx votingSitePrx = VotingSitePrx.checkedCast(
                    communicator.stringToProxy(proxyProperty));
            if (votingSitePrx == null) {
                System.err.println("No se pudo obtener el proxy de VotingSite.");
                return;
            }

            VotingNodeController controller = new VotingNodeController(votingSitePrx);
            VotingNodeImpl node = new VotingNodeImpl(controller);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Votar");
                System.out.println("2. 🔥");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (option == 1) {

                    System.out.print("Ingrese su ID de votante: ");
                    String voterId = scanner.nextLine();
                    System.out.print("Ingrese el ID del candidato: ");
                    String candidateId = scanner.nextLine();
                    node.votar(voterId, candidateId);

                } else if (option == 2) {

                    System.out.print("Ingrese el ID del nodo: ");
                    String nodeId = scanner.nextLine();
                    fire(node, nodeId);

                } else {
                    System.out.println("Opción no válida. Intente de nuevo.");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fire(VotingNodeImpl node, String nodeId) {
        for (int i = 0; i < 50000; i++) {
            node.votar(nodeId + "_voterId" + i, nodeId + "_candidateId" + i);
        }
    }

}
