package application.controllers;

import application.controllers.models.responseModels.BasicResponse;
import application.controllers.validators.CustomParseException;
import application.database.entities.UserEntity;
import application.database.services.AuctionService;
import application.database.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static application.configuration.ControllersConstants.CREATE_NEW_AUCTION_ENDPOINT;

/**
 * Created by DZONI on 01.11.2016.
 */

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class CreateNewAuctionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AuctionService auctionEntityService;
    @Autowired
    UserService userEntityService;

    @RequestMapping(value = CREATE_NEW_AUCTION_ENDPOINT, method = RequestMethod.POST)
    public BasicResponse createAuction(WebRequest webRequest,
                                       @RequestParam(value = "title", required = true) String title,
                                       @RequestParam(value = "endOfAuction", required = true) String endOfAuctionDate,
                                       @RequestParam(value = "loadDate", required = true) String loadDate,
                                       @RequestParam(value = "deliveryDate", required = true) String deliveryDate,
                                       @RequestParam(value = "locationFrom", required = true) String locationFrom,
                                       @RequestParam(value = "locationTo", required = true) String locationTo,
                                       @RequestParam(value = "length", required = true) String length,
                                       @RequestParam(value = "width", required = true) String width,
                                       @RequestParam(value = "height", required = true) String height,
                                       @RequestParam(value = "weight", required = true) String weight,
                                       @RequestParam(value = "description", required = true) String description,
                                       @RequestHeader(value = "token", required = true) String token) {
        try {
            UserEntity user = userEntityService.getUserByRefreshToken(token);
            auctionEntityService.createAuctionByGivenUserId(user, title, description, endOfAuctionDate, loadDate, deliveryDate, locationTo, locationFrom, length, width, height, weight, getMapWithOptionalParameters(webRequest));
        } catch (CustomParseException e) {
            return new BasicResponse(false, e.getMessage(), 1);
        } catch (NullPointerException e) {
            return new BasicResponse(false, e.getMessage(), 0);
        }
        return new BasicResponse(true, "Successful", 0);
    }

    private Map<String, String[]> getMapWithOptionalParameters(WebRequest webRequest) {
        return webRequest.getParameterMap();
    }
}
