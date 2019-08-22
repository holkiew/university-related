package application.database.services;

import application.configuration.WebPropertiesLoader;
import application.controllers.validators.AuctionValidator;
import application.controllers.validators.CustomParseException;
import application.database.daos.AuctionEntityDao;
import application.database.dtos.AuctionWithData;
import application.database.entities.AuctionEntity;
import application.database.entities.UserEntity;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by DZONI on 20.11.2016.
 */
@Component
public class AuctionService {

    @Autowired
    AuctionEntityDao auctionEntityDao;

    @Autowired
    WebPropertiesLoader webPropertiesLoader;

    public void createAuctionByGivenUserId(UserEntity userEntity, String title, String description, String endOfAuction, String loadDate, String deliveryDate, String fullLocationToAddress, String fullLocationFromAddress, String length, String width, String height, String weight, Map<String, String[]> additionalParameters) throws CustomParseException {
        AuctionEntity auctionEntity = AuctionValidator.parseAndCreateAuction(userEntity, title, description, endOfAuction, loadDate, deliveryDate, fullLocationToAddress, fullLocationFromAddress, length, width, height, weight, additionalParameters);
        auctionEntityDao.save(auctionEntity);
    }

    public List<AuctionEntity> getAuctionsByAmount(String _amount) throws NumberFormatException {
        int amount = ParseInt(_amount);
        if (validateRequestedAmount(amount) || amount > 0) {
            return auctionEntityDao.getQuantity(amount);
        } else {
            return Lists.newArrayList();
        }
    }

    public AuctionWithData getAuctionByIdWithAuctionData(String id) {
        AuctionEntity auction = auctionEntityDao.getById(ParseInt(id));
        if (auction instanceof AuctionEntity) {
            return new AuctionWithData(auction);
        } else
            return null;
    }

    private boolean validateRequestedAmount(int amount) {
        if (amount < 0 || amount > webPropertiesLoader.getMAX_DISPLAYED_AUCTIONS()) {
            return false;
        } else {
            return true;
        }
    }

    private Integer ParseInt(String amount) {
        try {
            return Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
