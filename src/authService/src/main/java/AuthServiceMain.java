import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import controller.AuthServiceController;
import controller.AuthServiceImpl;

public class AuthServiceMain {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("authService");

            // TODO: Reemplazar con la conexion a Redis
            // DBConnection dbConnection = new DBConnection(communicator);
            // dbConnection.connectDB();

            AuthServiceController controller = new AuthServiceController();
        
            AuthServiceImpl authService = new AuthServiceImpl(controller);

            adapter.add(authService, Util.stringToIdentity("authService"));
            adapter.activate();

            System.out.println("[INFO] Auth Service is running");

            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}