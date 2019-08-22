package application.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "bids", schema = "db", catalog = "")
public class BidsEntity {
    private int id;
    private BigDecimal offer;
    private UserEntity userEntity;
    private AuctionEntity auctionEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "offer", nullable = false, precision = 2)
    public BigDecimal getOffer() {
        return offer;
    }

    public void setOffer(BigDecimal offer) {
        this.offer = offer;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public AuctionEntity getAuctionEntity() {
        return auctionEntity;
    }

    public void setAuctionEntity(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidsEntity that = (BidsEntity) o;

        if (id != that.id) return false;
        if (offer != null ? !offer.equals(that.offer) : that.offer != null) return false;
        return userEntity != null ? userEntity.equals(that.userEntity) : that.userEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (offer != null ? offer.hashCode() : 0);
        result = 31 * result + (userEntity != null ? userEntity.hashCode() : 0);
        return result;
    }
}
