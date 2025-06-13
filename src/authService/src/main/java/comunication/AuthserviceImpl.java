package comunication;
import com.zeroc.Ice.Current;

import Contract.AuthService;
import controller.AuthServiceController;

public class AuthserviceImpl implements AuthService {

    private final AuthServiceController authServiceController;

    public AuthserviceImpl(AuthServiceController authServiceController) {
        this.authServiceController = authServiceController;
    }


    @Override
    public int authenticate(String voterId, Current current) {
        return authServiceController.authenticate(voterId);
    }

}
