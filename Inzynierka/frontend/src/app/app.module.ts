import {NgModule} from "@angular/core";
import {APP_PROVIDERS} from "./app.providers";
import {AppComponent} from "./app.component";
import {BrowserModule} from "@angular/platform-browser";
import {routing} from "./app.routing";
import {LoginModule} from "./login/login.module";
import {MainComponentsModule} from "./mainComponents/mainComponents.module";
import {HttpModule, JsonpModule} from "@angular/http";
import {LanguageChoiceComponent} from "./shared/components/language/languageChoice.component";

@NgModule({
    declarations: [
        AppComponent,
        LanguageChoiceComponent,
    ],
    imports: [
        BrowserModule,
        LoginModule,
        MainComponentsModule,
        HttpModule,
        JsonpModule,
        routing,
    ],
    providers: [APP_PROVIDERS],
    bootstrap: [AppComponent]
})
export class AppModule {
    constructor() {
        localStorage.setItem('logged', "false");
    }
}
