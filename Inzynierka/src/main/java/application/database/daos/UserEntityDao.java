package application.database.daos;

import application.database.entities.UserEntity;

/**
 * Created by DZONI on 09.10.2016.
 */

public interface UserEntityDao extends DaoSupport {

    UserEntity getById(int id);

    UserEntity getByUsernameAndPassword(String username, String password);

    UserEntity getUserByTokenRefresh(String token);

    UserEntity getUserByTokenAccess(String token);
}
