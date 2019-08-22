package application.services.codeGenerator;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by DZONI on 06.11.2016.
 */
@Service
public class RandomCodeGenerator {

    private final SecureRandom random = new SecureRandom();

    public String nextRandomString(int stringLength) {
        StringBuilder stringBuilder = new StringBuilder(new BigInteger(stringLength * 6, random).toString(32));
        if (stringBuilder.length() < stringLength) {
            stringBuilder.append(Strings.repeat("0", stringLength - stringBuilder.length()));
        }
        return stringBuilder.toString();
    }
}
