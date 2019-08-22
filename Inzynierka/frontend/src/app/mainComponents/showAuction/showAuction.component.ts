import {Component} from "@angular/core";
import {HTTPService} from "../../shared/services/HTTPService";
import {ActivatedRoute} from "@angular/router";
import {ShowAuctionMessages} from "../../shared/models/messages/showAuctionMessages";

@Component({
    moduleId: module.id,
    selector: 'as-show-auction',
    templateUrl: 'showAuction.html'
})
export class ShowAuctionComponent {
    private sub: any;
    private response: ShowAuctionMessages;
    constructor(private _HTTPService: HTTPService, private _activatedRoute: ActivatedRoute) {
    };

    ngOnInit() {
        this.sub = this._activatedRoute.params.subscribe(params => {
            // this.id = +params["id"]; // (+) converts string 'id' to a number
            this._HTTPService.getAuctionById(+params["id"])
                .subscribe(response => {this.response = response; console.log(response); });
        });
    }

    private ngOnDestroy() {
        this.sub.unsubscribe();
    }

}
