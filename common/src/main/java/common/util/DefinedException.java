package common.util;

public class DefinedException extends Exception {
    public DefinedException(Throwable throwable) {
        super(throwable);
    }

    public DefinedException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public DefinedException(String string) {
        super(string);
    }

    public DefinedException() {
        super();
    }
}
