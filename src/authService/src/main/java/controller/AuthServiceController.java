package controller;
import interfaces.IAuthServiceController;
import repository.AuthRepository;
import repository.DBConnection;
import com.zeroc.Ice.Communicator;

public class AuthServiceController implements IAuthServiceController {

    private final AuthRepository authRepository;

    public AuthServiceController(Communicator communicator) {
        try {
            this.authRepository = new AuthRepository(DBConnection.getInstance(communicator).connect());
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] No se pudo inicializar AuthRepository: " + e.getMessage(), e);
        }
    }

    @Override
    public int authenticate(String voterId, String tableId) {
        System.out.println("[INFO] [AUTHENTICATE]: " + voterId + " " + tableId);
        Boolean haVotado = authRepository.hasVoted(voterId);
        if (haVotado == null) {
            return 3; // no existe el ciudadano
        } else if (!haVotado) {
            return 0; // puede votar
        } else {
            return 2; // ya votó
        }
    }
}
