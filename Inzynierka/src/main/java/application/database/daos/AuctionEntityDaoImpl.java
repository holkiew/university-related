package application.database.daos;

import application.database.entities.AuctionEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DZONI on 09.11.2016.
 */

@Repository
public class AuctionEntityDaoImpl extends DaoSupportImpl implements AuctionEntityDao {

    @Override
    public List<AuctionEntity> getQuantity(int amount) {
        Session session = this.sessionFactory.openSession();
        List<AuctionEntity> resultList = session.getNamedQuery(AuctionEntity.GET_AUCTIONS).setMaxResults(amount).list();
        session.close();
        return resultList;
    }

    @Override
    public AuctionEntity getById(int id) {
        Session session = this.sessionFactory.openSession();
        AuctionEntity auction = (AuctionEntity) session.getNamedQuery(AuctionEntity.GET_AUCTION_BY_ID).setParameter("id", id).uniqueResult();
        session.close();
        return auction;
    }
}
