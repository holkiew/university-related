import {Injectable} from "@angular/core";
import {Http, URLSearchParams, Headers, Response} from "@angular/http";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {BasicResponse} from "../models/basicResponse";
import {CONSTANTS} from "../constant/constants";
import {Auction} from "../models/auction";
import {ShowAuctionMessages} from "../models/messages/showAuctionMessages";

@Injectable()
export class HTTPService {
    constructor(private _http: Http) {
    }

    login(login: string, password: string): Observable<BasicResponse> {
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('login', login);
        urlSearchParams.append('password', password);
        let params = urlSearchParams.toString();
        let headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        // console.log(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.LOGIN + "?" + params);
        return this._http.post(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.LOGIN, params, {
            headers: headers
        }).map((response: Response) => response.json());
    }

    createNewAuction(title: string, endOfAuctionDate: Date, loadDate: Date, deliveryDate: Date, fullLocationAddressTo: string, fullLocationAddressFrom: string,
                     length: number, width: number, height: number, weight: number, description: string, fragile: boolean, living: boolean, specialEnvironment: boolean): Observable<BasicResponse> {
        if (!JSON.parse(localStorage.getItem('logged'))) {
            return;
        }

        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('title', title);
        urlSearchParams.append('endOfAuction', endOfAuctionDate.toISOString());
        urlSearchParams.append('loadDate', loadDate.toISOString());
        urlSearchParams.append('deliveryDate', deliveryDate.toISOString());
        urlSearchParams.append('locationTo', fullLocationAddressTo);
        urlSearchParams.append('locationFrom', fullLocationAddressFrom);
        urlSearchParams.append('length', length.toString());
        urlSearchParams.append('width', width.toString());
        urlSearchParams.append('height', height.toString());
        urlSearchParams.append('weight', weight.toString());
        urlSearchParams.append('description', description);
        urlSearchParams.append('fragile', fragile.toString());
        urlSearchParams.append('living', living.toString());
        urlSearchParams.append('specialEnvironment', specialEnvironment.toString());

        let params = urlSearchParams.toString();
        let headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        headers.append('token', localStorage.getItem('refreshToken').toString());
        // console.log(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.CREATE_AUCTION + "?" + params);
        return this._http.post(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.CREATE_AUCTION, params, {
            headers: headers
        }).map((response: Response) => response.json());
    }

    createNewUser(username: string, password: string, email: string) {
        // return this._http.get("asd").map(errorResponse => errorResponse.json());
        // var usernameJSON = JSON.stringify({var: "username", username});
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', username);
        urlSearchParams.append('password', password);
        urlSearchParams.append('email', email);
        let params = urlSearchParams.toString();
        let headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        // console.log(SERVER_PATH+CREATE_USER+params);
        return this._http.post(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.CREATE_USER, params, {
            headers: headers
        }).map(response => response.json());
    }

    createNewUserResponse(username: string, password: string, email: string): Observable<BasicResponse> {
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', username);
        urlSearchParams.append('password', password);
        urlSearchParams.append('email', email);
        let params = urlSearchParams.toString();
        let headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        // console.log(SERVER_PATH+CREATE_USER+params);
        return this._http.post(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.CREATE_USER, params, {
            headers: headers
        }).map((response: Response) => response.json());
    }

    getAuctions(amount: number): Observable<Auction[]> {
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('amount', amount.toString());
        let headers = new Headers();
        let params = urlSearchParams.toString();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        // console.log(SERVER_PATH+LIST_AUCTIONS+"?"+ params);
        return this._http.get(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.LIST_AUCTIONS + "?" + params, {
            headers: headers
        }).map(response => response.json());
    }

    getAuctionById(id: number): Observable<ShowAuctionMessages> {
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('id', id.toString());
        let headers = new Headers();
        let params = urlSearchParams.toString();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        // console.log(SERVER_PATH+LIST_AUCTIONS+"?"+ params);
        return this._http.get(CONSTANTS.CONTEXT_VARIABLES.SERVER_PATH + CONSTANTS.SERVER_ENDPOINTS.GET_AUCTION + "?" + params, {
            headers: headers
        }).map(response => response.json());
    }
}
