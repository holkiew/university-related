package application.database.daos;

import application.database.entities.UserDataEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by DZONI on 30.10.2016.
 */
public class UserDataEntityDaoImpl extends DaoSupportImpl implements UserDataEntityDao {


    @SuppressWarnings("unchecked")
    @Override
    public List<UserDataEntity> list() {
        Session session = this.sessionFactory.openSession();
        List<UserDataEntity> usersData = session.createQuery("FROM UserData").list();
        session.close();
        return usersData;
    }
}
