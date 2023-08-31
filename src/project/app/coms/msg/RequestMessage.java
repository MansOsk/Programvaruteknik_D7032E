package project.app.coms.msg;

/**
 * Request and await response. Submit data and expect response from clients.
 * @author Måns Oskarsson mnsosk-7
 */
public class RequestMessage<T> extends Message<T>{
    public RequestMessage(T content, Instruction instruction) {
        super("REQUEST", content, instruction);
    }
}
