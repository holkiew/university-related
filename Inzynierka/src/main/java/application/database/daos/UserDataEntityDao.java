package application.database.daos;

import application.database.entities.UserDataEntity;

import java.util.List;

/**
 * Created by DZONI on 30.10.2016.
 */
public interface UserDataEntityDao extends DaoSupport {

        List<UserDataEntity> list();
}
