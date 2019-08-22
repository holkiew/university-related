package application.database.daos;

import application.database.entities.TokenAccessEntity;
import application.database.entities.TokenRefreshEntity;
import application.database.entities.UserEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by DZONI on 09.10.2016.
 */
//:TODO Ogarnac transactional, czy sa potrzebne transakcje

@Repository
public class UserEntityDaoImpl extends DaoSupportImpl implements UserEntityDao {

    @Override
    public UserEntity getById(int id) {
        Session session = this.sessionFactory.openSession();
        UserEntity userEntity = (UserEntity) session.getNamedQuery(UserEntity.GET_USER_BY_ID)
                .setParameter("id", id).uniqueResult();
        session.close();
        return userEntity;
    }

    @Override
    public UserEntity getByUsernameAndPassword(String username, String password) {
        Session session = this.sessionFactory.openSession();
        UserEntity userEntity = (UserEntity) session
                .getNamedQuery(UserEntity.GET_USER_BY_PASSWORD_AND_LOGIN)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        return userEntity;
    }

    //// TODO: 06.01.2017 One query to get UserToken!
    @Override
    public UserEntity getUserByTokenRefresh(String token) {
        Session session = this.sessionFactory.openSession();
        TokenRefreshEntity tokenRefreshEntity = (TokenRefreshEntity) session
                .getNamedQuery(UserEntity.GET_USER_BY_TOKEN_REFRESH)
                .setParameter("token", token)
                .uniqueResult();
        UserEntity userEntity = tokenRefreshEntity.getUserEntity();
        session.close();
        return userEntity;
    }

    //// TODO: 06.01.2017 One query to get UserToken!
    @Override
    public UserEntity getUserByTokenAccess(String token) {
        Session session = this.sessionFactory.openSession();
        TokenAccessEntity tokenAccessEntity = (TokenAccessEntity) session.getNamedQuery(UserEntity.GET_USER_BY_TOKEN_ACCESS)
                .setParameter("token", token)
                .uniqueResult();
        UserEntity userEntity = tokenAccessEntity.getUserEntity();
        session.close();
        return userEntity;
    }
}
