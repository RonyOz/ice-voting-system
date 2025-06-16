import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import Contract.ConsultServicePrx;
import java.util.Scanner;

public class ConsultNodeMain {
  public static void main(String[] args) {
    System.out.println("Iniciando ConsultNode...");
    Scanner scanner = new Scanner(System.in);
    ConsultServicePrx consultServicePrx = null;

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
<<<<<<< HEAD
      System.out.println("Communicator inicializado correctamente");
      
      // Intentar conectar al servicio
      try {
        System.out.println("Intentando conectar al servicio...");
        // Usar la configuración del properties.cfg
        consultServicePrx = ConsultServicePrx.checkedCast(
          communicator.propertyToProxy("ConsultService.Proxy")
        );
        
        if (consultServicePrx == null) {
          System.err.println("No se pudo conectar al servicio");
          throw new RuntimeException("No se pudo conectar al servicio de consulta");
        }
        
        System.out.println("Conexión exitosa al servicio de consulta");
      } catch (Exception e) {
        System.err.println("Error al conectar con el servicio: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }

      System.out.println("\n=== Sistema de Consulta de Ubicación de Votación ===");
      System.out.println("Consultando ID fijo: 711674049\n");

      try {
        String resultado = consultServicePrx.getVotingLocation("711674049");
        System.out.println("Ubicación de votación:");
        System.out.println(resultado + "\n");
      } catch (Exception e) {
        System.err.println("Error al consultar la ubicación: " + e.getMessage());
        e.printStackTrace();
      }

    } catch (Exception e) {
      System.err.println("Error fatal: " + e.getMessage());
=======
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
>>>>>>> 1b068d6ff1e5b502f0368c2eb9586bf78aa70cfb
      e.printStackTrace();
    } finally {
      scanner.close();
      System.out.println("Programa finalizado");
    }
  }
}
