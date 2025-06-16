import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import controller.AuthServiceController;
import comunication.AuthServiceImpl;

public class AuthServiceMain {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("AuthServiceAdapter");

            AuthServiceController controller = new AuthServiceController(communicator);
            AuthServiceImpl authService = new AuthServiceImpl(controller);

            adapter.add(authService, Util.stringToIdentity("default"));
            adapter.activate();

            System.out.println("[INFO] Auth Service is running");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                // aqui se podra cerrar la conexion a la base de datos
            }));

            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}