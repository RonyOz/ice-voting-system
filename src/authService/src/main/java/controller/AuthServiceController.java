package controller;
import interfaces.IVotingNodeController;
import Contract.Vote;
import Contract.VotingSitePrx;

public class AuthServiceController implements IAuthServiceController {

    private final VotingSitePrx votingSitePrx;

    //TODO: Hay que colocar con la conexion a redis, deptronto se pue hacer una carpeta repository para las conexiones a la base de datos y eso
    public VotingNodeController() {
    }

    // aqui debe implementarse la logica : Retorna 0 si puede votar; 1 si no es su mesa; 2 si está tratando de votar por segunda vez; 3 si no aparece en toda la bd
    @Override
    public int authenticate(String voterId) {

    }
}
