import {Component, NgZone} from "@angular/core";
import {CONSTANTS} from "../../constant/constants";

@Component({
    moduleId: module.id,
    selector: 'as-language-choice',
    templateUrl: 'languageChoice.html',
    styleUrls: ['languageChoice.css']
})

export class LanguageChoiceComponent {

    english: string = CONSTANTS.LANGUAGES.ENGLISH;
    polish: string = CONSTANTS.LANGUAGES.POLISH;

    constructor(private _ngZone: NgZone) {
    };

    setLanguage(language: string) {
        localStorage.setItem(CONSTANTS.LOCAL_STORAGE.LANGUAGE, language);
        this._ngZone.runOutsideAngular(() => {
            location.reload();
        });
    }
}
