package project.app.coms.msg;

/**
 * Message where no response of data is required.
 * @author Måns Oskarsson mnsosk-7
 */
public class StaticMessage<T> extends Message<T>{
    public StaticMessage(T content) {
        super("STATIC", content);
    }
    public StaticMessage(T content, Instruction instruction) {
        super("STATIC", content, instruction);
    }
}
