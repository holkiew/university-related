import {Component} from "@angular/core";
import {BasicResponse} from "../../shared/models/basicResponse";
import {HTTPService} from "../../shared/services/HTTPService";
import {Router} from "@angular/router";
import {CONSTANTS} from "../../shared/constant/constants";
import {DatetimeLocalParser} from "../../shared/utils/dateParser";

@Component({
    moduleId: module.id,
    selector: 'as-create-auction',
    templateUrl: 'createAuction.html',
    styleUrls: ['createAuction.css']
})
export class CreateAuctionComponent {
    title: string;
    endOfAuction: string;
    loadingDate: string;
    deliveryDate: string;
    length: number;
    width: number;
    height: number;
    weight: number;
    isLiving: boolean;
    isFragile: boolean;
    isSpecialEnvironment: boolean;
    description: string;
    locationTo: string;
    locationFrom: string;

    responseRead: boolean = false;
    logged: boolean;

    basicResponse: BasicResponse;
    errorResponse: string;

    constructor(private _HTTPService: HTTPService, private _router: Router, private _dateParser: DatetimeLocalParser) {
    };

    ngOnInit() {
        this.logged = JSON.parse(localStorage.getItem('logged'));
        if (!this.logged) {
            this._router.navigateByUrl("/" + CONSTANTS.LOCAL_ENDPOINTS.LOGIN);
        } else {
            this.initData();
        }
    }

    initData() {
        this.title = "";
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.weight = 0;
        this.description = "";
        this.locationFrom = "street address, city, country";
        this.locationTo = "street address, city, country";
        this.isFragile = false;
        this.isLiving = false;
        this.isSpecialEnvironment = false;
        this.endOfAuction = this._dateParser.dateToString(new Date());
        this.loadingDate = this._dateParser.dateToString(new Date());
        this.deliveryDate = this._dateParser.dateToString(new Date());
    }

    createNewAuction() {
        this._HTTPService.createNewAuction(this.title, this._dateParser.parseDate(this.endOfAuction), this._dateParser.parseDate(this.loadingDate), this._dateParser.parseDate(this.deliveryDate),
            this.locationTo, this.locationFrom, this.length, this.width, this.height, this.weight, this.description, this.isFragile, this.isLiving, this.isSpecialEnvironment)
            .subscribe((response: BasicResponse) => {
                    this.basicResponse = response;
                    this.responseRead = true;
                },
                error => this.errorResponse = <any>error);
    }
}
