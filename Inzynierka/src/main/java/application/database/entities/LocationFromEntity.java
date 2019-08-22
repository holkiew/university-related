package application.database.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "location_from", schema = "db", catalog = "")
public class LocationFromEntity {
    private int id;
    private String fullAddress;
    private String country;
    private AuctionEntity auctionEntity;

    @GenericGenerator(name = "auctionGenerator", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "auctionEntity"))
    @Id
    @GeneratedValue(generator = "auctionGenerator")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "full_address", nullable = false, length = 50)
    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Basic
    @Column(name = "country", nullable = false, length = 25)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
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

        LocationFromEntity that = (LocationFromEntity) o;

        if (id != that.id) return false;
        if (fullAddress != null ? !fullAddress.equals(that.fullAddress) : that.fullAddress != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return auctionEntity != null ? auctionEntity.equals(that.auctionEntity) : that.auctionEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fullAddress != null ? fullAddress.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (auctionEntity != null ? auctionEntity.hashCode() : 0);
        return result;
    }
}
