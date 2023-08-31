package project.app.exception;

/**
 * Wrapper for IOException. All input and output.
 * Provides better traceability and robustness.
 * @author Måns Oskarsson mnsosk-7
 */
public class A2AIOException extends A2AException{
    public A2AIOException(){}

    public A2AIOException(String msg){
        super(msg);
    }

    public A2AIOException(Throwable cause){
        super(cause);
    }

    public A2AIOException(String msg, Throwable cause){
        super(msg, cause);
    }

    public A2AIOException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
