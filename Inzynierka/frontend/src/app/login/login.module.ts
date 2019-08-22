import {LoginComponent} from "./login.component";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {HTTPService} from "../shared/services/HTTPService";

@NgModule({
    declarations: [
        LoginComponent
    ],
    imports: [
        FormsModule,
        CommonModule],
    providers: [HTTPService],
    exports: [
        LoginComponent
    ],
})
export class LoginModule {
}
