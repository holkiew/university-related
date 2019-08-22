package application.database.initialData.testingData;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by DZONI on 21.11.2016.
 */
public enum TestingAuctions {
    //String title, Date dateStart, Date dateEnd, boolean premium, boolean ended, UserEntity ownerUserEntity) {

    AUCTION1("Title1", new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 7, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), false, false, AuctionProperties.AUCTION_PROPERTIES1, AuctionData.AUCTION_DATA1),
    AUCTION2("Title2", new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 7, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), new Date(new DateTime(2016, 1, 1, 12, 00, 00).getMillis()), false, false, AuctionProperties.AUCTION_PROPERTIES2, AuctionData.AUCTION_DATA2);

    private String title;
    private Date dateStart;
    private Date dateEnd;
    private Date deliveryDate;
    private Date loadDate;
    private boolean premium;
    private boolean ended;
    private AuctionProperties auctionProperties;
    private AuctionData auctionData;

    TestingAuctions(String title, Date dateStart, Date dateEnd, Date deliveryDate, Date loadDate, boolean premium, boolean ended, AuctionProperties auctionProperties, AuctionData auctionData) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.deliveryDate = deliveryDate;
        this.loadDate = loadDate;
        this.premium = premium;
        this.ended = ended;
        this.auctionProperties = auctionProperties;
        this.auctionData = auctionData;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public boolean isPremium() {
        return premium;
    }

    public boolean isEnded() {
        return ended;
    }

    public AuctionProperties getAuctionProperties() {
        return auctionProperties;
    }

    public AuctionData getAuctionData() {
        return auctionData;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public enum AuctionData {
        AUCTION_DATA1("auction description1", "auction footer1"),
        AUCTION_DATA2("auction description2", "auction footer2");

        private String description;
        private String footer;

        AuctionData(String description, String footer) {
            this.description = description;
            this.footer = footer;
        }

        public String getDescription() {
            return description;
        }


        public String getFooter() {
            return footer;
        }

    }

    public enum AuctionProperties {
        AUCTION_PROPERTIES1(
                new BigDecimal(123.23), new BigDecimal(321.11), new BigDecimal(443), new BigDecimal(112),
                false, false, false, LocationFromTo.LOCATION_FROM_TO1),
        AUCTION_PROPERTIES2(
                new BigDecimal(123.23), new BigDecimal(5334.11), new BigDecimal(22221), new BigDecimal(123),
                false, false, false, LocationFromTo.LOCATION_FROM_TO2);
        private BigDecimal sizeX;
        private BigDecimal sizeY;
        private BigDecimal sizeZ;
        private BigDecimal weight;
        private boolean fragile;
        private boolean specialEnvironment;
        private boolean living;
        private LocationFromTo locationFromTo;

        AuctionProperties(BigDecimal sizeX, BigDecimal sizeY, BigDecimal sizeZ, BigDecimal weight, boolean fragile, boolean specialEnvironment, boolean living, LocationFromTo locationFromTo) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.sizeZ = sizeZ;
            this.weight = weight;
            this.fragile = fragile;
            this.specialEnvironment = specialEnvironment;
            this.living = living;
            this.locationFromTo = locationFromTo;
        }

        public BigDecimal getSizeX() {
            return sizeX;
        }

        public BigDecimal getSizeY() {
            return sizeY;
        }

        public BigDecimal getSizeZ() {
            return sizeZ;
        }

        public BigDecimal getWeight() {
            return weight;
        }


        public boolean isFragile() {
            return fragile;
        }

        public boolean isSpecialEnvironment() {
            return specialEnvironment;
        }

        public boolean isLiving() {
            return living;
        }

        public LocationFromTo getLocationFromTo() {
            return locationFromTo;
        }

        public enum LocationFromTo {
            LOCATION_FROM_TO1("fullAddressFrom1", "countryFrom1",
                    "fullAddressTo1", "countryTo1"),
            LOCATION_FROM_TO2("fullAddressFrom2", "countryFrom2",
                    "fullAddressTo2", "countryTo2");
            private String fullAddressFrom;
            private String countryFrom;
            private String fullAddressTo;
            private String countryTo;

            LocationFromTo(String fullAddressFrom, String countryFrom, String fullAddressTo, String countryTo) {
                this.fullAddressFrom = fullAddressFrom;
                this.countryFrom = countryFrom;
                this.fullAddressTo = fullAddressTo;
                this.countryTo = countryTo;
            }

            public String getFullAddressFrom() {
                return fullAddressFrom;
            }

            public String getCountryFrom() {
                return countryFrom;
            }

            public String getFullAddressTo() {
                return fullAddressTo;
            }

            public String getCountryTo() {
                return countryTo;
            }
        }
    }
}
