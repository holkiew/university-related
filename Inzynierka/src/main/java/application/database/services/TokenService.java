package application.database.services;

import application.database.daos.TokenEntityDao;
import application.database.daos.UserEntityDao;
import application.database.entities.TokenAccessEntity;
import application.database.entities.TokenRefreshEntity;
import application.database.entities.UserEntity;
import application.services.codeGenerator.RandomCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DZONI on 10.12.2016.
 */
@Service
public class TokenService {
    @Autowired
    UserEntityDao userEntityDao;

    @Autowired
    TokenEntityDao tokenEntityDao;

    @Autowired
    RandomCodeGenerator randomCodeGenerator;


    public void saveOrUpdateRefreshTokenForUser(UserEntity userEntity) {
        generateNewTokenString(userEntity, TokenRefreshEntity.class, 15, 86400);
        tokenEntityDao.update(userEntity.getRefreshTokenEntity());
        System.out.println(userEntity.getRefreshTokenEntity().getExpirationDate());
    }

    public void saveOrUpdateAccessTokenForUser(UserEntity userEntity) {
        generateNewTokenString(userEntity, TokenAccessEntity.class, 15, 60);
        tokenEntityDao.update(userEntity.getAccessTokenEntity());
    }

    private void generateNewTokenString(UserEntity user, Class clazz, int tokenLength, int durationSeconds) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(randomCodeGenerator.nextRandomString(tokenLength));
        stringBuilder.append(user.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, durationSeconds);

        if (clazz == TokenRefreshEntity.class) {
            user.getRefreshTokenEntity().setToken(stringBuilder.toString());
            user.getRefreshTokenEntity().setExpirationDate(new Date(calendar.getTimeInMillis()));
        } else if (clazz == TokenAccessEntity.class) {
            user.getAccessTokenEntity().setToken(stringBuilder.toString());
            user.getAccessTokenEntity().setExpirationDate(new Date(calendar.getTimeInMillis()));
        }
    }
}
