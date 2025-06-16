import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.IceGrid.QueryPrx;
import Contract.ConsultServicePrx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsultNodeMain {
  public static void main(String[] args) {
    System.out.println("Iniciando ConsultNode...");
    Scanner scanner = new Scanner(System.in);

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
      QueryPrx query = QueryPrx.checkedCast(
          communicator.stringToProxy("IceQuerySystem/Query"));

      ConsultServicePrx consultServicePrx = ConsultServicePrx.checkedCast(
          query.findObjectById(Util.stringToIdentity("ProxyCache")));

      if (consultServicePrx == null) {
        System.err.println("No se pudo obtener el ProxyCache desde IceGrid.");
        return;
      }

      System.out.println("Proxy conectado correctamente.");

      System.out.println("Seleccione el modo de operación:");
      System.out.println("1. Consulta manual");
      System.out.println("2. Procesar ciudadano.csv");

      String option = scanner.nextLine().trim();

      if (option.equals("1")) {
        while (true) {
          System.out.println("Por favor ingrese su número de cédula:");
          String id = scanner.nextLine().trim();

          try {
            String result = consultServicePrx.getVotingLocation(id);
            System.out.println("Resultado: " + result);
          } catch (Exception e) {
            System.err.println("Error al consultar el lugar de votación: " + e.getMessage());
          }

          System.out.println();
        }

      } else if (option.equals("2")) {
        try {

          InputStream inputStream = ConsultNodeMain.class.getClassLoader().getResourceAsStream("ciudadano.csv");
          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
          List<String> lines = reader.lines().collect(Collectors.toList());

          System.out.println("Procesando " + lines.size() + " cédulas desde ciudadano.csv...");

          long startTime = System.currentTimeMillis();

          for (int i = 0; i < 500; i++) {
            String documento = lines.get(i).trim();
            if (!documento.isEmpty()) {
              try {
                String result = consultServicePrx.getVotingLocation(documento);
                System.out.println("[" + documento + "] → " + result);
              } catch (Exception e) {
                System.err.println("[" + documento + "] Error: " + e.getMessage());
              }
            }
          }

          long endTime = System.currentTimeMillis();
          long totalTime = endTime - startTime;
          System.out.println("Tiempo total: " + totalTime + " ms");

        } catch (Exception e) {
          System.err.println("No se pudo leer el archivo ciudadano.csv: " + e.getMessage());
        }
      } else {
        System.out.println("Opción inválida.");
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
