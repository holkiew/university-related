package application.controllers;

import application.controllers.models.responseModels.BasicResponse;
import application.database.entities.TokenRefreshEntity;
import application.database.entities.UserEntity;
import application.database.services.TokenService;
import application.database.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static application.configuration.ControllersConstants.LOGIN_ENDPOINT;

/**
 * Created by DZONI on 03.12.2016.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userEntityService;

    @Autowired
    TokenService tokenService;

    @RequestMapping(value = LOGIN_ENDPOINT, method = RequestMethod.POST)
    public BasicResponse login(@RequestParam(value = "login", required = true) String login,
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "token", required = false) String token) {
        try {
            UserEntity userEntity = userEntityService.getUserByLoginAndPassword(login, password);
            TokenRefreshEntity refreshToken = userEntity.getRefreshTokenEntity();
            if (token == null) {
                tokenService.saveOrUpdateRefreshTokenForUser(userEntity);
                return new BasicResponse(true, refreshToken.getToken(), 1);
            } else {
                if (refreshToken.getToken().equals(token) && refreshToken.getExpirationDate().compareTo(new Date()) > 0) {
                    tokenService.saveOrUpdateAccessTokenForUser(userEntity);
                    return new BasicResponse(true, userEntity.getAccessTokenEntity().getToken(), 2);
                } else {
                    return new BasicResponse(false, "refresh token wrong or expired", 0);
                }
            }
        } catch (NullPointerException e) {
            return new BasicResponse(false, "Invalid token", 0);
        }


    }
}
