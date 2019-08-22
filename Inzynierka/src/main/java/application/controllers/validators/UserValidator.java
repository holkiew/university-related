package application.controllers.validators;

import application.database.entities.UserEntity;

/**
 * Created by DZONI on 01.11.2016.
 */
public class UserValidator {

    public static UserEntity parseAndCreateUser(String accountType, String company, String username, String password, String suspended, String language) throws CustomParseException {

        UserEntity user = new UserEntity();
        user.setUsername(parseUsername(username));
        user.setPassword(parsePassword(password));
        user.setSuspended(parseSuspended(suspended));
        return user;
    }


    private static int parseCompany(String company) throws CustomParseException {
        checkForUndefinedEmptyAndNull(company);
        return Integer.parseInt(company);
    }

    private static String parseUsername(String username) throws CustomParseException {
        checkForUndefinedEmptyAndNull(username);
        return username;
    }

    private static String parsePassword(String password) throws CustomParseException {
        checkForUndefinedEmptyAndNull(password);
        return password;
    }

    private static boolean parseSuspended(String accountType) throws CustomParseException {
        checkForUndefinedEmptyAndNull(accountType);
        return Boolean.parseBoolean(accountType);
    }

    private static int parseLanguage(String language) throws CustomParseException {
        checkForUndefinedEmptyAndNull(language);
        return Integer.parseInt(language);
    }

    private static void checkForUndefinedEmptyAndNull(String text) throws CustomParseException {
        if(text.equals("undefined") || text.equals("") || text == null) {
            throw new CustomParseException("ParseException: undefined or empty or null text:" + text);
        }
    }

}
