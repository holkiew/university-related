import {Routes} from "@angular/router";
import {CreateUserComponent} from "./createUser/createUser.component";
import {ListAuctionsComponent} from "./listAuctions/listAuctions.component";
import {CreateAuctionComponent} from "./createAuction/createAuction";
import {CONSTANTS} from "../shared/constant/constants";
import {ShowAuctionComponent} from "./showAuction/showAuction.component";

export const MainComponentsRoutes: Routes = [
    {path: CONSTANTS.LOCAL_ENDPOINTS.CREATE_AUCTION, component: CreateAuctionComponent},
    {path: CONSTANTS.LOCAL_ENDPOINTS.LIST_AUCTIONS, component: ListAuctionsComponent},
    {path: CONSTANTS.LOCAL_ENDPOINTS.CREATE_USER, component: CreateUserComponent},
    {path: CONSTANTS.LOCAL_ENDPOINTS.SHOW_AUCTION + "/:id", component: ShowAuctionComponent}
];
