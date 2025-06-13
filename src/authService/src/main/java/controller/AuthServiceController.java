package controller;
import interfaces.IAuthServiceController;

public class AuthServiceController implements IAuthServiceController {


    //TODO: Hay que colocar con la conexion a redis, deptronto se pue hacer una carpeta repository para las conexiones a la base de datos y eso
    public AuthServiceController() {
    }

    // aqui debe implementarse la logica : Retorna 0 si puede votar; 1 si no es su mesa; 2 si está tratando de votar por segunda vez; 3 si no aparece en toda la bd
    @Override
    public int authenticate(String voterId, String tableId) {
        System.out.println("[INFO] [AUTHENTICATE]: " + voterId + " " + tableId);
        return 0;
    }
}
