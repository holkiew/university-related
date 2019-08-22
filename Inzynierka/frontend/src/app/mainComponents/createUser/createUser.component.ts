import {Component} from "@angular/core";
import {BasicResponse} from "../../shared/models/basicResponse";
import {HTTPService} from "../../shared/services/HTTPService";

@Component({
    moduleId: module.id,
    selector: 'as-create-user',
    templateUrl: 'createUser.html',
    styleUrls: ['createUser.css']
})
export class CreateUserComponent {
    public name: string;
    public password: string;
    public email: string;
    public basicResponse: BasicResponse;
    public response: string;
    public responseRead: boolean = false;

    constructor(private _HTTPService: HTTPService) {
    };

    private createNewUser() {
        this._HTTPService.createNewUser(this.name, this.password, this.email)
            .subscribe(
                data => this.response = JSON.stringify(data),
                error => alert(error),
                () => console.log("finished")
            );
    }

    private createNewUserResponse() {
        this._HTTPService.createNewUserResponse(this.name, this.password, this.email)
            .subscribe((response: BasicResponse) => {
                    this.basicResponse = response;
                    this.responseRead = true;
                },
                error => this.response = <any>error);

    }
}
