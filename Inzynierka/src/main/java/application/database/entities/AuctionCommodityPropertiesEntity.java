package application.database.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "auction_properties", schema = "db", catalog = "")
public class AuctionCommodityPropertiesEntity {
    private int id;

    private BigDecimal sizeX;
    private BigDecimal sizeY;
    private BigDecimal sizeZ;
    private BigDecimal weight;
    private boolean fragile;
    private boolean specialEnvironment;
    private boolean living;
    private AuctionEntity auctionEntity;
    private CategoryEntity categoryEntity;

    private Set<ImageEntity> imageEntities;

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
    @Column(name = "sizeX", nullable = false, precision = 8, scale = 2)
    public BigDecimal getSizeX() {
        return sizeX;
    }

    public void setSizeX(BigDecimal sizeX) {
        this.sizeX = sizeX;
    }

    @Basic
    @Column(name = "sizeY", nullable = false, precision = 8, scale = 2)
    public BigDecimal getSizeY() {
        return sizeY;
    }

    public void setSizeY(BigDecimal sizeY) {
        this.sizeY = sizeY;
    }

    @Basic
    @Column(name = "sizeZ", nullable = false, precision = 8, scale = 2)
    public BigDecimal getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(BigDecimal sizeZ) {
        this.sizeZ = sizeZ;
    }

    @Basic
    @Column(name = "weight", nullable = false, precision = 8, scale = 2)
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "fragile", nullable = false)
    public boolean getFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    @Basic
    @Column(name = "specialEnviroment", nullable = false)
    public boolean getSpecialEnviroment() {
        return specialEnvironment;
    }

    public void setSpecialEnviroment(boolean specialEnvironment) {
        this.specialEnvironment = specialEnvironment;
    }

    @Basic
    @Column(name = "living", nullable = false)
    public boolean getLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public AuctionEntity getAuctionEntity() {
        return auctionEntity;
    }

    public void setAuctionEntity(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @OneToMany(mappedBy = "auctionCommodityPropertiesEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    public void setImageEntities(Set<ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionCommodityPropertiesEntity that = (AuctionCommodityPropertiesEntity) o;

        if (id != that.id) return false;
        if (fragile != that.fragile) return false;
        if (specialEnvironment != that.specialEnvironment) return false;
        if (living != that.living) return false;
        if (sizeX != null ? !sizeX.equals(that.sizeX) : that.sizeX != null) return false;
        if (sizeY != null ? !sizeY.equals(that.sizeY) : that.sizeY != null) return false;
        if (sizeZ != null ? !sizeZ.equals(that.sizeZ) : that.sizeZ != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (auctionEntity != null ? !auctionEntity.equals(that.auctionEntity) : that.auctionEntity != null)
            return false;
        if (categoryEntity != null ? !categoryEntity.equals(that.categoryEntity) : that.categoryEntity != null)
            return false;
        return imageEntities != null ? imageEntities.equals(that.imageEntities) : that.imageEntities == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sizeX != null ? sizeX.hashCode() : 0);
        result = 31 * result + (sizeY != null ? sizeY.hashCode() : 0);
        result = 31 * result + (sizeZ != null ? sizeZ.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (fragile ? 1 : 0);
        result = 31 * result + (specialEnvironment ? 1 : 0);
        result = 31 * result + (living ? 1 : 0);
        result = 31 * result + (auctionEntity != null ? auctionEntity.hashCode() : 0);
        result = 31 * result + (categoryEntity != null ? categoryEntity.hashCode() : 0);
        result = 31 * result + (imageEntities != null ? imageEntities.hashCode() : 0);
        return result;
    }
}
