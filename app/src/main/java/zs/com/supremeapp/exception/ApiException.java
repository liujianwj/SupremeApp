package zs.com.supremeapp.exception;

/**
 * Created by liujian on 2018/9/4.
 */

public class ApiException extends RuntimeException{

    public ApiException() {
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

}
