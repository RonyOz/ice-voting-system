import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.IceGrid.QueryPrx;
import Contract.ConsultServicePrx;
import java.util.Scanner;

public class ConsultNodeMain {
  public static void main(String[] args) {
    System.out.println("Iniciando ConsultNode...");
    Scanner scanner = new Scanner(System.in);

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
      // Buscar el proxy por su ID exacto
      QueryPrx query = QueryPrx.checkedCast(
          communicator.stringToProxy("IceQuerySystem/Query"));

      ConsultServicePrx consultServicePrx = ConsultServicePrx.checkedCast(
          query.findObjectById(Util.stringToIdentity("ProxyCache")) // usamos el ID del objeto proxy cache
      );

      if (consultServicePrx == null) {
        System.err.println("No se pudo obtener el ProxyCache desde IceGrid.");
        return;
      }

      System.out.println("Proxy conectado correctamente. Entrando al bucle principal...");

      // Main program loop
      while (true) {
        System.out.println("Por favor ingrese su número de cédula:");
        String id = scanner.nextLine().trim();

        try {
          String result = consultServicePrx.getVotingLocation(id);
          System.out.println("Resultado: " + result);
        } catch (Exception e) {
          System.err.println("Error al consultar el lugar de votación: " + e.getMessage());
        }

        System.out.println(); // línea en blanco
      }

    } catch (Exception e) {
      System.err.println("Error de conexión con Ice: " + e.getMessage());
      e.printStackTrace();
    } finally {
      scanner.close();
      System.out.println("Programa finalizado");
    }
  }
}
