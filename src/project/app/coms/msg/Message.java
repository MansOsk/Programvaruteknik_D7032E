package project.app.coms.msg;

import java.io.Serializable;

/**
 * The template for constructing messages.
 * Implementation of Serializable since ObjectOutputStream requires this if pushing this object to object stream.
 * @param <T> What type of unique content the message has, can be cards, boolean, string etc.
 * @author Måns Oskarsson mnsosk-7
 */
public abstract class Message<T> implements Serializable {

    private final String type;
    private T content;
    private Instruction instruction;

    public Message(String type, T content){
        this.type = type;
        this.content = content;
    }

    public Message(String type, T content, Instruction instruction){
        this.type = type;
        this.content = content;
        this.instruction = instruction;
    }

    public String msgType(){
        return this.type;
    }

    public T getContent(){
        return this.content;
    }

    public void setContent(T obj){
        this.content = obj;
    }

    public boolean isObject(){
        return this.content != null;
    }

    public Instruction getInstruction(){
        return this.instruction;
    }
}
