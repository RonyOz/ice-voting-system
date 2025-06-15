import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import com.zeroc.IceGrid.QueryPrx;

import Contract.ConsultServicePrx;

import java.util.Scanner;

public class ConsultNodeMain {
  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      ConsultServicePrx consultServicePrx = null;
      QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("IceQuerySystem/Query"));
      consultServicePrx = ConsultServicePrx.checkedCast(query.findObjectByType("::Contract::ConsultService"));

      System.out.println("Going into main loop");

      // Main program loop
      while (true) {
        System.out.println("Porfavor ingrese su numero de cedula");
        String ID = scanner.next();
        System.out.println("Resultado:" + consultServicePrx.getVotingLocation(ID));
        scanner.nextLine();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    scanner.close();
  }

}
