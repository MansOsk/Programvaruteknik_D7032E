package project.app.exception;

/**
 * Exception used in network func. Connection interrupted etc.
 * Provides better traceability and robustness.
 * @author Måns Oskarsson mnsosk-7
 */
public class A2ANetworkException extends A2AException{
    public A2ANetworkException(){}

    public A2ANetworkException(String msg){
        super(msg);
    }

    public A2ANetworkException(Throwable cause){
        super(cause);
    }

    public A2ANetworkException(String msg, Throwable cause){
        super(msg, cause);
    }

    public A2ANetworkException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
