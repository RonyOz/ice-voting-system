import Contract.VotingSitePrx;
import comunication.VotingNodeImpl;
import controller.VotingNodeController;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectAdapter;


import java.util.Scanner;

public class VotingNode {
    public static void main(String[] args) {



        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            ObjectAdapter adapter = communicator.createObjectAdapter("VoteStation");


            String proxyProperty = communicator.getProperties().getProperty("VotingSite.Proxy");
            VotingSitePrx votingSitePrx = VotingSitePrx.checkedCast(
                    communicator.stringToProxy(proxyProperty)
            );
            if (votingSitePrx == null) {
                System.err.println("No se pudo obtener el proxy de VotingSite.");
                return;
            }

            VotingNodeController controller = new VotingNodeController(votingSitePrx);
            VotingNodeImpl node = new VotingNodeImpl(controller);

            adapter.add(node, Util.stringToIdentity("VoteStation"));


            // Scanner scanner = new Scanner(System.in);
            // while (true) {
            //     System.out.print("Ingrese su ID de votante: ");
            //     String voterId = scanner.nextLine();
            //     System.out.print("Ingrese el ID del candidato: ");
            //     String candidateId = scanner.nextLine();
            //     node.votar(voterId, candidateId);
            // }


            for (int i = 0; i < 100000; i++) {
                node.voteCLI("voterId" + i, "candidateId" + i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
