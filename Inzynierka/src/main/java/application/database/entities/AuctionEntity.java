package application.database.entities;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = AuctionEntity.GET_AUCTIONS, query = "FROM AuctionEntity act"),
        @NamedQuery(name = AuctionEntity.GET_AUCTION_BY_ID, query = "FROM AuctionEntity act WHERE act.id = :id")
})
@Table(name = "auction", schema = "db", catalog = "")
public class AuctionEntity {
    public static final String GET_AUCTIONS = "GET_AUCTIONS";
    public static final String GET_AUCTION_BY_ID = "GET_AUCTION_BY_ID";
    private int id;
    private String title;
    private Date auctionStartDate;
    private Date auctionEndDate;
    private Date loadDate;
    private Date deliveryDate;
    private boolean premium;
    private boolean ended;
    private UserEntity ownerUserEntity;
    private UserEntity wonUserEntity;
    private AuctionDataEntity auctionDataEntity;
    private AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity;
    private LocationFromEntity locationFromEntity;
    private LocationToEntity locationToEntity;
    private Set<BidsEntity> bidsEntities;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auction_generator")
    @SequenceGenerator(name = "auction_generator", sequenceName = "hibernate_auction_generator", allocationSize = 1)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "auction_start_date", nullable = false, columnDefinition = "DATETIME")
    public Date getAuctionStartDate() {
        return auctionStartDate;
    }

    public void setAuctionStartDate(Date dateStart) {
        this.auctionStartDate = dateStart;
    }

    @Basic
    @Column(name = "auction_end_date", nullable = false, columnDefinition = "DATETIME")
    public Date getAuctionEndDate() {
        return auctionEndDate;
    }

    public void setAuctionEndDate(Date dateEnd) {
        this.auctionEndDate = dateEnd;
    }

    @Basic
    @Column(name = "load_date", nullable = false, columnDefinition = "DATETIME")
    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    @Basic
    @Column(name = "delivery_date", nullable = false, columnDefinition = "DATETIME")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Basic
    @Column(name = "premium", nullable = false)
    public boolean getPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @Basic
    @Column(name = "ended", nullable = false)
    public boolean getEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getOwnerUserEntity() {
        return ownerUserEntity;
    }

    public void setOwnerUserEntity(UserEntity ownerUserEntity) {
        this.ownerUserEntity = ownerUserEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getWonUserEntity() {
        return wonUserEntity;
    }

    public void setWonUserEntity(UserEntity wonUserEntity) {
        this.wonUserEntity = wonUserEntity;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "auctionEntity", cascade = {CascadeType.ALL})
    public AuctionDataEntity getAuctionDataEntity() {
        return auctionDataEntity;
    }

    public void setAuctionDataEntity(AuctionDataEntity auctionDataEntity) {
        this.auctionDataEntity = auctionDataEntity;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "auctionEntity", cascade = {CascadeType.ALL})
    public AuctionCommodityPropertiesEntity getAuctionCommodityPropertiesEntity() {
        return auctionCommodityPropertiesEntity;
    }

    public void setAuctionCommodityPropertiesEntity(AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity) {
        this.auctionCommodityPropertiesEntity = auctionCommodityPropertiesEntity;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "auctionEntity", cascade = CascadeType.ALL)
    public LocationFromEntity getLocationFromEntity() {
        return locationFromEntity;
    }

    public void setLocationFromEntity(LocationFromEntity locationFromEntity) {
        this.locationFromEntity = locationFromEntity;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "auctionEntity", cascade = CascadeType.ALL)
    public LocationToEntity getLocationToEntity() {
        return locationToEntity;
    }

    public void setLocationToEntity(LocationToEntity locationEntityTo) {
        this.locationToEntity = locationEntityTo;
    }

    @OneToMany(mappedBy = "auctionEntity", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    public Set<BidsEntity> getBidsEntities() {
        return bidsEntities;
    }

    public void setBidsEntities(Set<BidsEntity> bidsEntities) {
        this.bidsEntities = bidsEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionEntity that = (AuctionEntity) o;

        if (id != that.id) return false;
        if (premium != that.premium) return false;
        if (ended != that.ended) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (auctionStartDate != null ? !auctionStartDate.equals(that.auctionStartDate) : that.auctionStartDate != null)
            return false;
        if (auctionEndDate != null ? !auctionEndDate.equals(that.auctionEndDate) : that.auctionEndDate != null)
            return false;
        if (ownerUserEntity != null ? !ownerUserEntity.equals(that.ownerUserEntity) : that.ownerUserEntity != null)
            return false;
        if (wonUserEntity != null ? !wonUserEntity.equals(that.wonUserEntity) : that.wonUserEntity != null)
            return false;
        if (auctionDataEntity != null ? !auctionDataEntity.equals(that.auctionDataEntity) : that.auctionDataEntity != null)
            return false;
        return auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.equals(that.auctionCommodityPropertiesEntity) : that.auctionCommodityPropertiesEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (auctionStartDate != null ? auctionStartDate.hashCode() : 0);
        result = 31 * result + (auctionEndDate != null ? auctionEndDate.hashCode() : 0);
        result = 31 * result + (premium ? 1 : 0);
        result = 31 * result + (ended ? 1 : 0);
        result = 31 * result + (ownerUserEntity != null ? ownerUserEntity.hashCode() : 0);
        result = 31 * result + (wonUserEntity != null ? wonUserEntity.hashCode() : 0);
        result = 31 * result + (auctionDataEntity != null ? auctionDataEntity.hashCode() : 0);
        result = 31 * result + (auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.hashCode() : 0);
        return result;
    }
}
