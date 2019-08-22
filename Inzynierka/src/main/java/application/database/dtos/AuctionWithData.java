package application.database.dtos;

import application.configuration.utils.CustomJSONDateSerializer;
import application.database.entities.AuctionEntity;
import application.database.entities.BidsEntity;
import application.database.entities.CategoryEntity;
import application.database.entities.ImageEntity;
import application.database.utils.HibernateUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by DZONI on 04.12.2016.
 */
public class AuctionWithData {
    private int id;
    private String title;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date startOfAuctionDate;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date endOfAuction;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date loadDate;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date deliveryDate;
    private boolean premium;
    private boolean ended;
    private String description;
    private String footer;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal weight;
    private boolean fragile;
    private boolean specialEnvironment;
    private boolean living;
    private String fullAddressFrom;
    private String countryFrom;
    private String fullAddressTo;
    private String countryTo;

    private CategoryEntity categoryEntity;
    private Set<BidsEntity> bidsEntities;
    private Set<ImageEntity> imageEntities;

    public AuctionWithData(AuctionEntity auctionEntity) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        this.id = auctionEntity.getId();
        this.title = auctionEntity.getTitle();
        this.startOfAuctionDate = auctionEntity.getAuctionStartDate();
        this.endOfAuction = auctionEntity.getAuctionEndDate();
        this.loadDate = auctionEntity.getLoadDate();
        this.deliveryDate = auctionEntity.getDeliveryDate();
        this.premium = auctionEntity.getPremium();
        this.ended = auctionEntity.getEnded();
        this.description = auctionEntity.getAuctionDataEntity().getDescription();
        this.footer = auctionEntity.getAuctionDataEntity().getFooter();
        this.length = auctionEntity.getAuctionCommodityPropertiesEntity().getSizeX();
        this.width = auctionEntity.getAuctionCommodityPropertiesEntity().getSizeY();
        this.height = auctionEntity.getAuctionCommodityPropertiesEntity().getSizeZ();
        this.weight = auctionEntity.getAuctionCommodityPropertiesEntity().getWeight();
        this.fragile = auctionEntity.getAuctionCommodityPropertiesEntity().getFragile();
        this.specialEnvironment = auctionEntity.getAuctionCommodityPropertiesEntity().getSpecialEnviroment();
        this.living = auctionEntity.getAuctionCommodityPropertiesEntity().getLiving();
        this.fullAddressFrom = auctionEntity.getLocationFromEntity().getFullAddress();
        this.countryFrom = auctionEntity.getLocationFromEntity().getCountry();
        this.fullAddressTo = auctionEntity.getLocationToEntity().getFullAddress();
        this.countryTo = auctionEntity.getLocationToEntity().getCountry();
        session.close();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartOfAuctionDate() {
        return startOfAuctionDate;
    }

    public void setStartOfAuctionDate(Date startOfAuctionDate) {
        this.startOfAuctionDate = startOfAuctionDate;
    }

    public Date getEndOfAuction() {
        return endOfAuction;
    }

    public void setEndOfAuction(Date endOfAuction) {
        this.endOfAuction = endOfAuction;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public boolean isSpecialEnvironment() {
        return specialEnvironment;
    }

    public void setSpecialEnvironment(boolean specialEnvironment) {
        this.specialEnvironment = specialEnvironment;
    }

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    public String getFullAddressFrom() {
        return fullAddressFrom;
    }

    public void setFullAddressFrom(String fullAddressFrom) {
        this.fullAddressFrom = fullAddressFrom;
    }

    public String getCountryFrom() {
        return countryFrom;
    }

    public void setCountryFrom(String countryFrom) {
        this.countryFrom = countryFrom;
    }

    public String getFullAddressTo() {
        return fullAddressTo;
    }

    public void setFullAddressTo(String fullAddressTo) {
        this.fullAddressTo = fullAddressTo;
    }

    public String getCountryTo() {
        return countryTo;
    }

    public void setCountryTo(String countryTo) {
        this.countryTo = countryTo;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
