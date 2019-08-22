package application.controllers;

import application.database.dtos.AuctionWithData;
import application.database.services.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static application.configuration.ControllersConstants.GET_AUCTION_ENDPOINT;

/**
 * Created by DZONI on 03.12.2016.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class GetAuctionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AuctionService auctionEntityService;

    @RequestMapping(value = GET_AUCTION_ENDPOINT, method = RequestMethod.GET)
    public AuctionWithData getAuctionById(@RequestParam(value = "id", required = true) String id) {
        AuctionWithData auction = auctionEntityService.getAuctionByIdWithAuctionData(id);
        return auction;
    }
}
