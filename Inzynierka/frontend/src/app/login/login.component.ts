import {Component, SimpleChanges} from "@angular/core";
import {BasicResponse} from "../shared/models/basicResponse";
import {HTTPService} from "../shared/services/HTTPService";
import {CONSTANTS} from "../shared/constant/constants";
import {LoginMessages} from "../shared/models/messages/loginMessages";
import {LanguageMessagesReader} from "../shared/services/languageMessageReader";

@Component({
    moduleId: module.id,
    selector: 'as-login-form',
    templateUrl: 'login.html',
    styleUrls: ['login.css']
})
export class LoginComponent {
    username: string;
    password: string;
    logged: boolean;
    basicResponse: BasicResponse;

    messages: LoginMessages;

    constructor(private _HTTPService: HTTPService, private _JSONReader: LanguageMessagesReader) {
    };

    ngOnInit() {
        this.logged = JSON.parse(localStorage.getItem('logged'));
        this._JSONReader.readLanguageFile(CONSTANTS.LANGUAGES.MESSAGE_FILE_NAMES.LOGIN)
            .subscribe(response => {
                this.messages = response;
            });
    }

    ngOnChanges(changes: SimpleChanges) {
        // changes.prop contains the old and the new value...
        console.log(changes.toString());
    }

    login() {
        this._HTTPService.login(this.username, this.password)
            .subscribe(response => {
                this.basicResponse = response;
                this.storeCredentials();
            });
    }

    storeCredentials() {
        if (this.basicResponse.successful) {
            localStorage.setItem("refreshToken", this.basicResponse.responseMessage);
            this.logged = true;
        }
    }

    logout() {
        if (this.logged) {
            localStorage.setItem("refreshToken", null);
            localStorage.setItem("logged", "false");
            this.logged = false;
        }
    }
}
