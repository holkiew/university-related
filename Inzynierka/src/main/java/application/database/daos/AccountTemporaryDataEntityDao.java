package application.database.daos;

import application.database.entities.AccountTemporaryDataEntity;
import application.database.entities.UserEntity;

/**
 * Created by DZONI on 08.11.2016.
 */
public interface AccountTemporaryDataEntityDao extends DaoSupport {

    AccountTemporaryDataEntity getByUserEntity(UserEntity userEntity);
}
