package application.configuration;

/**
 * Created by DZONI on 08.11.2016.
 */

public class ControllersConstants {
    //--------------------------------ENDPOINTS--------------------------------
    public static final String CREATE_USER_ENDPOINT = "createUser";
    public static final String ACTIVATE_USER_USER_ENDPOINT = "activateUser";
    public static final String CREATE_NEW_AUCTION_ENDPOINT = "createAuction";
    public static final String LIST_AUCTIONS_ENDPOINT = "listAuctions";
    public static final String LOGIN_ENDPOINT = "login";
    public static final String GET_AUCTION_ENDPOINT = "getAuction";

    //-------------------------------------------------------------------------

    public static final int ACTIVATION_STRING_LENGTH = 10;
    public static final String NEW_USER_MAIL_TEXT_TEMPLATE = "Welcome,\nAccount name: %s\nPassword: %s\nActivation code: %s";
    public static final String NEW_USER_MAIL_SUBJECT_TEMPLATE = "Activation code mail";
    public static final String ACTIVATION_URL_ADDRESS_LINK_TEMPLATE = "%s" + ACTIVATE_USER_USER_ENDPOINT + "/%s/%s";
    public static final String DATE_FORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_OUTPUT = "yyyy-MM-dd HH:mm:ss+02:00";

    //-------------------------------------------------------------------------

    public static class CommodityProperties {
        public static final String LIVING = "living";
        public static final String SPECIAL_ENVIRONMENT = "specialEnvironment";
        public static final String FRAGILE = "fragile";

    }
}
