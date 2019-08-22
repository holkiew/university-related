package application.database.daos;

import application.database.entities.AccountTemporaryDataEntity;
import application.database.entities.UserEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by DZONI on 08.11.2016.
 */
@Repository

public class AccountTemporaryDataEntityDaoImpl extends DaoSupportImpl implements AccountTemporaryDataEntityDao {

    @Override
    public AccountTemporaryDataEntity getByUserEntity(UserEntity userEntity) {
        Session session = this.sessionFactory.openSession();
        AccountTemporaryDataEntity accountTemporaryDataEntity = (AccountTemporaryDataEntity) session.getNamedQuery(AccountTemporaryDataEntity.GET_USER_TEMP_DATA_BY_ID).setParameter("userId", userEntity.getId()).uniqueResult();
        session.close();
        return accountTemporaryDataEntity;
    }
}
