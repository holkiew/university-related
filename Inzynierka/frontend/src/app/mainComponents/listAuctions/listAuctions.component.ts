import {Component} from "@angular/core";
import {Auction} from "../../shared/models/auction";
import {HTTPService} from "../../shared/services/HTTPService";
import {Router} from "@angular/router";
import {CONSTANTS} from "../../shared/constant/constants";

@Component({
    moduleId: module.id,
    selector: 'as-list-auctions',
    templateUrl: 'listAuctions.html'
})
export class ListAuctionsComponent {
    auctions: Auction[];

    constructor(private _HTTPService: HTTPService, private _router: Router) {
    };

    getAuctions() {
        this._HTTPService.getAuctions(50)
            .subscribe(response => this.auctions = response);
    }

    ngOnInit() {
        this.getAuctions();
    }

    navigateToAuction(id) {
        console.log(id);
        this._router.navigate([CONSTANTS.LOCAL_ENDPOINTS.SHOW_AUCTION, id]);
    }

}
