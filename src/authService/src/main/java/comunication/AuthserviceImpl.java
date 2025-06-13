package comunication;
import com.zeroc.Ice.Current;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import Contract.AuthService;
import controller.AuthServiceController;

public class AuthserviceImpl implements AuthService {

    private final AuthServiceController authServiceController;
    private final ExecutorService threadPool;

    public AuthserviceImpl(AuthServiceController authServiceController) {
        this.authServiceController = authServiceController;
        // Crete the thread pool with the number of processors available
        this.threadPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        );
    }

    @Override
    public int authenticate(String voterId, String tableId, Current current) {
        try {
            Future<Integer> future = threadPool.submit(() -> 
                authServiceController.authenticate(voterId, tableId)
            );
            
            // Timeout of 3 seconds for the authentication
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("[ERROR] Error en autenticación: " + e.getMessage());
            throw new RuntimeException("Error en la autenticación: " + e.getMessage());
        }
    }

    // Method to close the ThreadPool when the service stops
    public void shutdown() {
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
