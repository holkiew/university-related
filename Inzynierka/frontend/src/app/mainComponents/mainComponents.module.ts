import {CreateUserComponent} from "./createUser/createUser.component";
import {CreateAuctionComponent} from "./createAuction/createAuction";
import {ListAuctionsComponent} from "./listAuctions/listAuctions.component";
import {FormsModule} from "@angular/forms";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DirectionMapComponent} from "../shared/components/google/maps/directions/directionMap.component";
import {DatetimeLocalParser} from "../shared/utils/dateParser";
import {ShowAuctionComponent} from "./showAuction/showAuction.component";

@NgModule({
    declarations: [
        CreateUserComponent,
        CreateAuctionComponent,
        ListAuctionsComponent,
        DirectionMapComponent,
        ShowAuctionComponent
    ],
    imports: [
        FormsModule,
        CommonModule
    ],
    providers: [DatetimeLocalParser],
    exports: [
        CreateUserComponent,
        CreateAuctionComponent,
        ListAuctionsComponent
    ]
})
export class MainComponentsModule {
}
