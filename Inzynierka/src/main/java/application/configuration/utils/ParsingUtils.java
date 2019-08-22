package application.configuration.utils;

import application.configuration.ControllersConstants;
import application.controllers.validators.CustomParseException;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by DZONI on 06.01.2017.
 */
public class ParsingUtils {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(ControllersConstants.DATE_FORMAT_INPUT);

    public static BigDecimal parseStringToBigDecimal(String number) {
        return new BigDecimal(number.replace(',', '.'));
    }

    public static Date parseDate(String date) throws CustomParseException {
        try {
            return new Date(dateFormatter.parse(date).getTime());
        } catch (java.text.ParseException e) {
            throw new CustomParseException(e.getClass().getName());
        }
    }
}
