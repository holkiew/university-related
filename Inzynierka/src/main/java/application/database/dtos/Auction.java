package application.database.dtos;

import application.configuration.utils.CustomJSONDateSerializer;
import application.database.entities.AuctionEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Created by DZONI on 03.12.2016.
 */
public class Auction {
    private int id;
    private String title;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date dateStart;
    @JsonSerialize(using = CustomJSONDateSerializer.class)
    private Date dateEnd;
    private boolean premium;
    private boolean ended;

    public Auction(AuctionEntity auctionEntity) {
        this.id = auctionEntity.getId();
        this.title = auctionEntity.getTitle();
        this.dateStart = auctionEntity.getAuctionStartDate();
        this.dateEnd = auctionEntity.getAuctionEndDate();
        this.premium = auctionEntity.getPremium();
        this.ended = auctionEntity.getEnded();
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

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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
}
