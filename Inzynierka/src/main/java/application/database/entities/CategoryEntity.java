package application.database.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "category", schema = "db", catalog = "")
public class CategoryEntity {
    private int id;
    private String name;
    private Set<AuctionCommodityPropertiesEntity> auctionCommodityPropertiesEntity;

    public CategoryEntity() {
    }

    public CategoryEntity(int id, String name) {
        this.id = id;
        this.name = name;
        this.auctionCommodityPropertiesEntity = new HashSet<>();
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 35)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "categoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<AuctionCommodityPropertiesEntity> getAuctionCommodityPropertiesEntity() {
        return auctionCommodityPropertiesEntity;
    }

    public void setAuctionCommodityPropertiesEntity(Set<AuctionCommodityPropertiesEntity> auctionCommodityPropertiesEntity) {
        this.auctionCommodityPropertiesEntity = auctionCommodityPropertiesEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntity that = (CategoryEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.equals(that.auctionCommodityPropertiesEntity) : that.auctionCommodityPropertiesEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (auctionCommodityPropertiesEntity != null ? auctionCommodityPropertiesEntity.hashCode() : 0);
        return result;
    }
}
