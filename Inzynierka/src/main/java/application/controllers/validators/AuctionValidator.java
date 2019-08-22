package application.controllers.validators;

import application.configuration.ControllersConstants;
import application.configuration.utils.ParsingUtils;
import application.database.entities.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by DZONI on 09.11.2016.
 */
//// TODO: 06.01.2017 Parsing labriaries, agains HTML, Java or language
public class AuctionValidator {

    public static AuctionEntity parseAndCreateAuction(UserEntity userEntity, String title, String description, String endOfAuction, String loadDate, String deliveryDate, String fullLocationToAddress, String fullLocationFromAddress, String length, String width, String height, String weight, Map<String, String[]> additionalParameters) throws CustomParseException {
        AuctionEntity auctionEntity = initAuction(title, endOfAuction, loadDate, deliveryDate);
        AuctionDataEntity auctionDataEntity = initAuctionData(description);
        AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity = initAuctionCommodityProperties(length, width, height, weight, additionalParameters);
        LocationFromEntity locationFromEntity = initLocationFrom(fullLocationFromAddress);
        LocationToEntity locationToEntity = initLocationTo(fullLocationToAddress);

        bindEntities(userEntity, auctionEntity, auctionDataEntity, auctionCommodityPropertiesEntity, locationFromEntity, locationToEntity);
        return auctionEntity;
    }

    private static AuctionEntity initAuction(String title, String endOfAuction, String loadDate, String deliveryDate) throws CustomParseException {
        AuctionEntity auction = new AuctionEntity();
        auction.setTitle(parseTitle(title));
        auction.setAuctionStartDate(new Date(Calendar.getInstance().getTimeInMillis()));
        auction.setAuctionEndDate(ParsingUtils.parseDate(endOfAuction));
        auction.setLoadDate(ParsingUtils.parseDate(loadDate));
        auction.setDeliveryDate(ParsingUtils.parseDate(deliveryDate));
        auction.setEnded(false);
        auction.setPremium(false);
        return auction;
    }

    private static AuctionCommodityPropertiesEntity initAuctionCommodityProperties(String length, String width, String height, String weight, Map<String, String[]> additionalParameters) throws CustomParseException {
        AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity = new AuctionCommodityPropertiesEntity();
        try {
            auctionCommodityPropertiesEntity.setSizeX(ParsingUtils.parseStringToBigDecimal(length));
            auctionCommodityPropertiesEntity.setSizeY(ParsingUtils.parseStringToBigDecimal(width));
            auctionCommodityPropertiesEntity.setSizeZ(ParsingUtils.parseStringToBigDecimal(height));
            auctionCommodityPropertiesEntity.setWeight(ParsingUtils.parseStringToBigDecimal(weight));
            //// TODO: 06.01.2017 Additional arguments should be constants, smartly placed somewhere
            auctionCommodityPropertiesEntity.setFragile(Boolean.parseBoolean(getArgumentFromMap(additionalParameters, ControllersConstants.CommodityProperties.FRAGILE)));
            auctionCommodityPropertiesEntity.setSpecialEnviroment(Boolean.parseBoolean(getArgumentFromMap(additionalParameters, ControllersConstants.CommodityProperties.SPECIAL_ENVIRONMENT)));
            auctionCommodityPropertiesEntity.setLiving(Boolean.parseBoolean(getArgumentFromMap(additionalParameters, ControllersConstants.CommodityProperties.LIVING)));
        } catch (NumberFormatException e) {
            throw new CustomParseException(e.getClass().getName());
        }
        return auctionCommodityPropertiesEntity;
    }

    private static AuctionDataEntity initAuctionData(String description) {
        AuctionDataEntity auctionDataEntity = new AuctionDataEntity();
        auctionDataEntity.setDescription(parseDescription(description));
        auctionDataEntity.setFooter("");
        return auctionDataEntity;
    }

    //// TODO: 06.01.2017 Extracting city from full string
    private static LocationToEntity initLocationTo(String fullLocationFromAddress){
        LocationToEntity locationToEntity = new LocationToEntity();
        locationToEntity.setFullAddress(fullLocationFromAddress);
        locationToEntity.setCountry("");
        return locationToEntity;
    }

    //// TODO: 06.01.2017 Extracting city from full string
    private static LocationFromEntity initLocationFrom(String fullLocationToAddress){
        LocationFromEntity locationFromEntity = new LocationFromEntity();
        locationFromEntity.setFullAddress(fullLocationToAddress);
        locationFromEntity.setCountry("");
        return locationFromEntity;
    }

    private static void bindEntities(UserEntity userEntity, AuctionEntity auctionEntity, AuctionDataEntity auctionDataEntity, AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity, LocationFromEntity locationFromEntity, LocationToEntity locationToEntity) {
        auctionEntity.setOwnerUserEntity(userEntity);

        auctionEntity.setAuctionDataEntity(auctionDataEntity);
        auctionDataEntity.setAuctionEntity(auctionEntity);

        auctionEntity.setAuctionCommodityPropertiesEntity(auctionCommodityPropertiesEntity);
        auctionCommodityPropertiesEntity.setAuctionEntity(auctionEntity);

        locationFromEntity.setAuctionEntity(auctionEntity);
        auctionEntity.setLocationFromEntity(locationFromEntity);

        locationToEntity.setAuctionEntity(auctionEntity);
        auctionEntity.setLocationToEntity(locationToEntity);
    }

    private static String getArgumentFromMap(Map<String, String[]> map, String key) {
        try {
            return map.get(key)[0];
        } catch (NullPointerException e) {
            return null;
        }
    }

    private static String parseTitle(String language) {
        return language;
    }

    private static String parseDescription(String description) {
        return description;
    }

}
