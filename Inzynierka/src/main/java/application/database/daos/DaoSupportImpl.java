package application.database.daos;

import application.database.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by DZONI on 10.12.2016.
 */
public abstract class DaoSupportImpl implements DaoSupport {

    protected SessionFactory sessionFactory;

    @PostConstruct
    private void setDefaultSessionFactory() {
        this.sessionFactory = HibernateUtil.getSessionAnnotationFactory();
    }

    @Override
    public void save(Object object) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(object);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Object object) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(object);
        transaction.commit();
        session.close();
    }

    @Override
    public void batchInsert(Set<?> objects) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        objects.forEach(obj -> session.persist(obj));
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Object object) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(object);
        transaction.commit();
        session.close();
    }

    @Override
    public void saveOrUpdate(Object object) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(object);
        transaction.commit();
        session.close();
    }

}
