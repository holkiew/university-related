package application.controllers;

import application.database.dtos.Auction;
import application.database.entities.AuctionEntity;
import application.database.services.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static application.configuration.ControllersConstants.LIST_AUCTIONS_ENDPOINT;

/**
 * Created by DZONI on 01.11.2016.
 */

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ListAuctionsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AuctionService auctionEntityService;

    @RequestMapping(value = LIST_AUCTIONS_ENDPOINT, method = RequestMethod.GET)
    public List<Auction> listAuctions(@RequestParam(value = "amount", required = true) String amountOfAuctions) {
        List<AuctionEntity> auctions = auctionEntityService.getAuctionsByAmount(amountOfAuctions);
        List<Auction> temp = new ArrayList<>();
        auctions.forEach(a -> temp.add(new Auction(a)));
        return temp;
    }
}

