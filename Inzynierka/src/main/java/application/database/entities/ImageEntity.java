package application.database.entities;

import javax.persistence.*;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "image", schema = "db", catalog = "")
public class ImageEntity {
    private int id;
    private String path;
    private AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity;

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
    @Column(name = "path", nullable = false, length = 40)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public AuctionCommodityPropertiesEntity getAuctionCommodityPropertiesEntity() {
        return auctionCommodityPropertiesEntity;
    }

    public void setAuctionCommodityPropertiesEntity(AuctionCommodityPropertiesEntity auctionCommodityPropertiesEntity) {
        this.auctionCommodityPropertiesEntity = auctionCommodityPropertiesEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageEntity that = (ImageEntity) o;

        if (id != that.id) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.equals(that.auctionCommodityPropertiesEntity) : that.auctionCommodityPropertiesEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.hashCode() : 0);
        return result;
    }
}
