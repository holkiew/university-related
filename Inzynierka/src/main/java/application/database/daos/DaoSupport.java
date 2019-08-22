package application.database.daos;

import java.util.Set;

/**
 * Created by DZONI on 10.12.2016.
 */
public interface DaoSupport {

    void save(Object object);

    void update(Object object);

    void batchInsert(Set<?> objects);

    void delete(Object object);

    void saveOrUpdate(Object object);
}
