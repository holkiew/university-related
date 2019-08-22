package application.database.services;

import application.configuration.WebPropertiesLoader;
import application.controllers.validators.CustomParseException;
import application.controllers.validators.UserValidator;
import application.database.daos.AccountTemporaryDataEntityDao;
import application.database.daos.UserEntityDao;
import application.database.entities.*;
import application.services.codeGenerator.RandomCodeGenerator;
import application.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;

import static application.configuration.ControllersConstants.*;

/**
 * Created by DZONI on 08.11.2016.
 */
@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserEntityDao userEntityDao;
    @Autowired
    AccountTemporaryDataEntityDao accountTemporaryDataEntityDao;
    @Autowired
    MailService mailService;
    @Autowired
    RandomCodeGenerator randomCodeGenerator;
    @Autowired
    WebPropertiesLoader webPropertiesLoader;
    @Autowired
    TokenService tokenService;

    public UserEntity getUserEntityById(String id) throws NumberFormatException, NullPointerException {
        UserEntity userEntity = userEntityDao.getById(Integer.parseInt(id));
        if (userEntity instanceof UserEntity) {
            return userEntity;
        } else
            throw new NullPointerException("No matching user");
    }

    public UserEntity createNewUserAndSendActivationMail(
            String accountType, String company, String username, String password, String language, String email) throws CustomParseException, MailException {

        UserEntity userEntity = UserValidator.parseAndCreateUser(accountType, company, username, password, "true", language);
        userEntity.setEmail(email);

        AccountTemporaryDataEntity accountTemporaryData = new AccountTemporaryDataEntity();
        accountTemporaryData.setActivationCode(randomCodeGenerator.nextRandomString(ACTIVATION_STRING_LENGTH));


        userEntity.setAccountTemporaryDataEntity(accountTemporaryData);
        accountTemporaryData.setUserEntity(userEntity);

        createRefreshTokenEntityForUser(userEntity);
        createAccessTokenEntityForUser(userEntity);

        userEntityDao.save(userEntity);

        mailService.sendMail(userEntity.getEmail(), NEW_USER_MAIL_SUBJECT_TEMPLATE, String.format(NEW_USER_MAIL_TEXT_TEMPLATE, userEntity.getUsername(), userEntity.getPassword(),
                String.format(ACTIVATION_URL_ADDRESS_LINK_TEMPLATE, webPropertiesLoader.getCONTEXT_PATH(), userEntity.getId(), accountTemporaryData.getActivationCode())));
        //:TODO cofniecie stworzenia konta jesli nie wyslano maila

        return userEntity;
    }

    public AuctionEntity createNewAuction(UserEntity userEntity, String title, Date dateStart, Date dateEnd, String premium, String ended) {
        AuctionEntity auctionEntity = new AuctionEntity();

        return auctionEntity;
    }

    public boolean activateUser(UserEntity userEntity, String activationCode) {
        AccountTemporaryDataEntity userTempDataEntity = accountTemporaryDataEntityDao.getByUserEntity(userEntity);
        if (userTempDataEntity.getActivationCode().equals(activationCode)) {
            //:TODO zmiana stanu usera kiedy zostaje aktywowany
//            userEntity.setSuspended(false);
//            userEntity.setAccountTemporaryDataEntity(userTempDataEntity);
//            userTempDataEntity.setUserEntity(userEntity);
//            userEntityDao.save(userEntity);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUserByLoginAndPassword(String login, String password) {
        if (userEntityDao.getByUsernameAndPassword(login, password) instanceof UserEntity) {
            return true;
        } else
            return false;

    }

    public UserEntity getUserByLoginAndPassword(String login, String password) throws NullPointerException {
        UserEntity userEntity = userEntityDao.getByUsernameAndPassword(login, password);
        checkNull(userEntity, "No user with given credentials");
        return userEntity;
    }

    public UserEntity getUserByRefreshToken(String token) {
        UserEntity userEntity = userEntityDao.getUserByTokenRefresh(token);
        checkNull(userEntity, "No user of given token");
        return userEntity;
    }

    public UserEntity getUserByAccessToken(String token) {
        UserEntity userEntity = userEntityDao.getUserByTokenAccess(token);
        checkNull(userEntity, "No user of given token");
        return userEntity;
    }

    private void checkNull(Object object, String message) throws NullPointerException {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    private void createRefreshTokenEntityForUser(UserEntity userEntity) {
        TokenRefreshEntity tokenEntity = new TokenRefreshEntity();
        tokenEntity.setToken("initialRefresh" + userEntity.getUsername());
        tokenEntity.setExpirationDate(new Date(Calendar.getInstance().getTimeInMillis()));
        userEntity.setRefreshTokenEntity(tokenEntity);
        tokenEntity.setUserEntity(userEntity);
    }

    private void createAccessTokenEntityForUser(UserEntity userEntity) {
        TokenAccessEntity tokenEntity = new TokenAccessEntity();
        tokenEntity.setToken("initialAccess" + userEntity.getUsername());
        tokenEntity.setExpirationDate(new Date(Calendar.getInstance().getTimeInMillis()));
        userEntity.setAccessTokenEntity(tokenEntity);
        tokenEntity.setUserEntity(userEntity);
    }


}
