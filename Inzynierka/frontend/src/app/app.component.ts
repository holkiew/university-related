import {Component} from "@angular/core";
import {CONSTANTS} from "./shared/constant/constants";

@Component({
    moduleId: module.id,
    selector: 'as-main-app',
    templateUrl: 'app.html'
})
export class AppComponent {
    appBrand: string;

    constructor() {
        this.appBrand = CONSTANTS.APP.BRAND;
        // DEBUG
        if (true) {
            localStorage.setItem(CONSTANTS.LOCAL_STORAGE.TOKEN_REFRESH, "RefreshToken1");
            localStorage.setItem(CONSTANTS.LOCAL_STORAGE.LOGGED, "true");
        }
    }
}
