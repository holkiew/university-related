package application.database.daos;

import application.database.entities.AuctionEntity;

import java.util.List;

/**
 * Created by DZONI on 09.11.2016.
 */
public interface AuctionEntityDao extends DaoSupport {

    List<AuctionEntity> getQuantity(int amount);

    AuctionEntity getById(int id);
}
