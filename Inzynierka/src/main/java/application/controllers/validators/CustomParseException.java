package application.controllers.validators;

/**
 * Created by DZONI on 20.11.2016.
 */
public class CustomParseException extends Exception {
    public CustomParseException(String exceptionText) {
        super(exceptionText);
    }
}
