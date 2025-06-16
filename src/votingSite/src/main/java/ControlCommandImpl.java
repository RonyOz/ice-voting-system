import Contract.*;
import java.util.Timer;
import java.util.TimerTask;

public class ControlCommandImpl implements ControlCommand {
    private static boolean isVotingActive = false;
    private static Timer shutdownTimer = new Timer();
    private static boolean shouldShutdown = false;

    static {
        // Configurar el timer para verificar el estado cada 5 segundos
        shutdownTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (shouldShutdown) {
                    System.out.println("[SHUTDOWN] Voting site is shutting down...");
                    System.exit(0);
                }
            }
        }, 0, 5000); // Verificar cada 5 segundos
    }

    @Override
    public void receiveCommand(String command, com.zeroc.Ice.Current current) {
        switch (command) {
            case "START_VOTATION":
                isVotingActive = true;
                shouldShutdown = false;
                System.out.println("[RECEIVED] START_VOTATION");
                break;
            case "END_VOTATION":
                isVotingActive = false;
                shouldShutdown = true;
                System.out.println("[RECEIVED] END_VOTATION");
                break;
            case "SEND_VOTE":
                if (isVotingActive) {
                    System.out.println("[RECEIVED] SEND_VOTE");
                } else {
                    System.out.println("[IGNORED] SEND_VOTE (voting not active)");
                }
                break;
            default:
                System.out.println("[ERROR] Unknown command: " + command);
        }
    }
} 