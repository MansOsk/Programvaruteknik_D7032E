package project.app.exception;

/**
 * Exception created for this application Apples2apples.
 * <a href="https://www.baeldung.com/java-new-custom-exception">...</a>
 * @author Måns Oskarsson mnsosk-7
 */
public class A2AException extends Exception{
    public A2AException(){}

    public A2AException(String msg){
        super(msg);
    }

    public A2AException(Throwable cause){
        super(cause);
    }

    public A2AException(String msg, Throwable cause){
        super(msg, cause);
    }

    public A2AException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
