package project.app.exception;

/**
 * Used when reading files, if bounds for index in reading x amount of lines is incorrect.
 * Provides better traceability and robustness.
 * @author Måns Oskarsson mnsosk-7
 */
public class A2ABoundsException extends A2AException{
    public A2ABoundsException(){}

    public A2ABoundsException(String msg){
        super(msg);
    }

    public A2ABoundsException(Throwable cause){
        super(cause);
    }

    public A2ABoundsException(String msg, Throwable cause){
        super(msg, cause);
    }

    public A2ABoundsException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
