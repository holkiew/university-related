package application.database.initialData;

import application.configuration.WebPropertiesLoader;
import application.database.daos.*;
import application.database.entities.*;
import application.database.initialData.initialData.InitialAccountTypes;
import application.database.initialData.initialData.InitialCategories;
import application.database.initialData.initialData.InitialLanguages;
import application.database.initialData.initialData.InitialStatusTypes;
import application.database.initialData.testingData.TestingAuctions;
import application.database.initialData.testingData.TestingUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DZONI on 08.11.2016.
 */
@Component
public class InitialDatabaseInserts {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AccountTypeEntityDao accountTypeEntityDao;
    @Autowired
    CategoryEntityDao categoryEntityDao;
    @Autowired
    LanguageEntityDao languageEntityDao;
    @Autowired
    StatusTypeEntityDao statusTypeEntityDao;
    @Autowired
    UserEntityDao userEntityDao;
    @Autowired
    AuctionEntityDao auctionEntityDao;
    @Autowired
    WebPropertiesLoader webPropertiesLoader;

    @PostConstruct
    public void init() {
        insertAccountTypes();
        insertCategories();
        insertLanguages();
        insertStatusTypes();
        testingObjectsInit();
    }

    public void testingObjectsInit() {
        if (webPropertiesLoader.getDEBUG_INSERTS()) {
            insertTestingUsers();
            insertTestingAuctions();
        }
    }

    private void insertAccountTypes() {
        Set<AccountTypeEntity> accountTypeEntities = new HashSet<>();
        for (InitialAccountTypes value : InitialAccountTypes.values()) {
            accountTypeEntities.add(new AccountTypeEntity(value.getId(), value.getName()));
        }
        accountTypeEntityDao.batchInsert(accountTypeEntities);
        logger.info("Initial insert: Inserted " + accountTypeEntities.size() + " accountTypeEntities");
    }

    private void insertCategories() {
        Set<CategoryEntity> categoryEntities = new HashSet<>();
        for (InitialCategories value : InitialCategories.values()) {
            categoryEntities.add(new CategoryEntity(value.getId(), value.getName()));
        }
        categoryEntityDao.batchInsert(categoryEntities);
        logger.info("Initial insert: Inserted " + categoryEntities.size() + " categoryEntities");
    }

    private void insertLanguages() {
        Set<LanguageEntity> languageEntities = new HashSet<>();
        for (InitialLanguages value : InitialLanguages.values()) {
            languageEntities.add(new LanguageEntity(value.getId(), value.getName()));
        }
        languageEntityDao.batchInsert(languageEntities);
        logger.info("Initial insert: Inserted " + languageEntities.size() + " languageEntities");
    }

    private void insertStatusTypes() {
        Set<StatusTypeEntity> statusTypeEntities = new HashSet<>();
        for (InitialStatusTypes value : InitialStatusTypes.values()) {
            statusTypeEntities.add(new StatusTypeEntity(value.getId(), value.getName()));
        }
        statusTypeEntityDao.batchInsert(statusTypeEntities);
        logger.info("Initial insert: Inserted " + statusTypeEntities.size() + " statusTypeEntities");
    }

    private void insertTestingUsers() {
        for (TestingUsers value : TestingUsers.values()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(value.getUsername());
            userEntity.setPassword(value.getPassword());
            userEntity.setSuspended(value.isSuspended());
            userEntity.setEmail(value.getEmail());

            //REFRESH TOKENS
            TokenAccessEntity accessTokenEntity = new TokenAccessEntity();
            accessTokenEntity.setToken(value.getAccessToken().getToken());
            accessTokenEntity.setExpirationDate(value.getAccessToken().getExpireDate());
            accessTokenEntity.setUserEntity(userEntity);
            userEntity.setAccessTokenEntity(accessTokenEntity);

            //ACCESS TOKENS
            TokenRefreshEntity refreshTokenEntity = new TokenRefreshEntity();
            refreshTokenEntity.setToken(value.getRefreshToken().getToken());
            refreshTokenEntity.setExpirationDate(value.getRefreshToken().getExpireDate());
            refreshTokenEntity.setUserEntity(userEntity);
            userEntity.setRefreshTokenEntity(refreshTokenEntity);

            userEntityDao.save(userEntity);
        }
        logger.warn("Initial insert: Inserted TESTING users");
    }

    private void insertTestingAuctions() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        for (TestingAuctions value : TestingAuctions.values()) {
            AuctionEntity auctionEntity = new AuctionEntity();
            auctionEntity.setTitle(value.getTitle());
            auctionEntity.setAuctionStartDate(value.getDateStart());
            auctionEntity.setAuctionEndDate(value.getDateEnd());
            auctionEntity.setDeliveryDate(value.getDeliveryDate());
            auctionEntity.setLoadDate(value.getLoadDate());
            auctionEntity.setPremium(value.isPremium());
            auctionEntity.setEnded(value.isEnded());
            auctionEntity.setOwnerUserEntity(userEntity);

            //AUCTION DATA
            AuctionDataEntity auctionDataEntity = new AuctionDataEntity();
            auctionDataEntity.setDescription(value.getAuctionData().getDescription());
            auctionDataEntity.setFooter(value.getAuctionData().getFooter());
            auctionEntity.setAuctionDataEntity(auctionDataEntity);
            auctionDataEntity.setAuctionEntity(auctionEntity);

            //AUCTION PROPERTIES
            AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity = new AuctionCommodityPropertiesEntity();

            auctionCommodityPropertiesEntity.setFragile(value.getAuctionProperties().isFragile());
            auctionCommodityPropertiesEntity.setLiving(value.getAuctionProperties().isLiving());
            auctionCommodityPropertiesEntity.setSpecialEnviroment(value.getAuctionProperties().isSpecialEnvironment());
            auctionCommodityPropertiesEntity.setSizeX(value.getAuctionProperties().getSizeX());
            auctionCommodityPropertiesEntity.setSizeY(value.getAuctionProperties().getSizeY());
            auctionCommodityPropertiesEntity.setSizeZ(value.getAuctionProperties().getSizeZ());
            auctionCommodityPropertiesEntity.setWeight(value.getAuctionProperties().getWeight());
            auctionEntity.setAuctionCommodityPropertiesEntity(auctionCommodityPropertiesEntity);
            auctionCommodityPropertiesEntity.setAuctionEntity(auctionEntity);

            //LOCATION FROM
            LocationFromEntity locationEntityFrom = new LocationFromEntity();
            locationEntityFrom.setFullAddress(value.getAuctionProperties().getLocationFromTo().getFullAddressFrom());
            locationEntityFrom.setCountry(value.getAuctionProperties().getLocationFromTo().getCountryFrom());
            auctionEntity.setLocationFromEntity(locationEntityFrom);
            locationEntityFrom.setAuctionEntity(auctionEntity);

            //LOCATION TO
            LocationToEntity locationEntityTo = new LocationToEntity();
            locationEntityTo.setFullAddress(value.getAuctionProperties().getLocationFromTo().getFullAddressTo());
            locationEntityTo.setCountry(value.getAuctionProperties().getLocationFromTo().getCountryTo());
            auctionEntity.setLocationToEntity(locationEntityTo);
            locationEntityTo.setAuctionEntity(auctionEntity);

            auctionEntityDao.save(auctionEntity);
        }
        logger.warn("Initial insert: Inserted TESTING auctions");
    }
}
