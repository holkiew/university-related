import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import "rxjs/add/operator/map";
import {CONSTANTS} from "../constant/constants";

@Injectable()
export class LanguageMessagesReader {
    private readonly relativePathToSources: string = "src/app/shared/constant/languagesData/";

    constructor(private _http: Http) {
    }

    readLanguageFile(fileName: string) {
        let relativePathToData = this.relativePathToSources;
        relativePathToData += localStorage.getItem(CONSTANTS.LOCAL_STORAGE.LANGUAGE) + "/" + fileName + ".json";
        return this._http.request(relativePathToData)
            .map(res => res.json());
    }
}
