package comunication;
import com.zeroc.Ice.Current;

import Contract.VotingNode;
import controller.VotingNodeController;

public class VotingNodeImpl implements VotingNode {

    private final VotingNodeController votingNodeController;

    public VotingNodeImpl(VotingNodeController votingNodeController) {
        this.votingNodeController = votingNodeController;
    }

    // despues se debe validar los diferentes casos y colocar los mensajes tipo
    // ERROR_ENVÍO: document  motivo TIMEOUT o EXCEPCIÓN
    @Override
    public int vote(String document, String candidateId, Current current) {
        return votingNodeController.vote(document, candidateId);
    }

    public int voteCLI(String document, String candidateId) {
        return votingNodeController.vote(document, candidateId);
    }

}
