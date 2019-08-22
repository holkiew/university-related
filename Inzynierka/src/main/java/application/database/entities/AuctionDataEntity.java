package application.database.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "auction_data", schema = "db", catalog = "")
public class AuctionDataEntity {
    private int id;
    private String description;
    private String footer;
    private AuctionEntity auctionEntity;

    @Id
    @GenericGenerator(name = "auctionGenerator", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "auctionEntity"))
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "auctionGenerator")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "footer", nullable = true, length = 200)
    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public AuctionEntity getAuctionEntity() {
        return auctionEntity;
    }

    public void setAuctionEntity(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
    }
}
