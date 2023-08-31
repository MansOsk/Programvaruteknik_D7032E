package project.app.exception;

/**
 * Traceability and robustness when constructing messages on different places.
 */
public class A2AMessageException extends A2AException{
    public A2AMessageException(){}

    public A2AMessageException(String msg){
        super(msg);
    }

    public A2AMessageException(Throwable cause){
        super(cause);
    }

    public A2AMessageException(String msg, Throwable cause){
        super(msg, cause);
    }

    public A2AMessageException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
