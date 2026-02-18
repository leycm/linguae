package de.leycm.linguae.exeption;

public class IncompatibleMatchException extends IllegalArgumentException {
    public IncompatibleMatchException(Class<?> type, Throwable cause) {
        super("Serializer for type " + type.getName() + " returned incompatible type", cause);
    }
}
