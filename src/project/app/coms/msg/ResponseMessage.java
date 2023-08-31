package project.app.coms.msg;

/**
 * Responds to request message.
 * @author Måns Oskarsson mnsosk-7
 */
public class ResponseMessage<T> extends Message<T>{
    public ResponseMessage(T content) {
        super("RESPONSE", content);
    }
}
