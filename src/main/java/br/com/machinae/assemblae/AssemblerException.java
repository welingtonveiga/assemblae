package br.com.machinae.assemblae;

/**
 * Generic Exception for Assemblae2 API calls.
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @since 02/11/2012
 */
public class AssemblerException extends RuntimeException {

    /**
     * @see RuntimeException
     */
    public AssemblerException() {
        super();
    }

    /**
     * @see RuntimeException
     */
    public AssemblerException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException
     */
    public AssemblerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see RuntimeException
     */
    public AssemblerException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException
     */
    protected AssemblerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
