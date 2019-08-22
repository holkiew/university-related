import {Routes} from "@angular/router";
import {LoginComponent} from "./login.component";
import {CONSTANTS} from "../shared/constant/constants";

export const LoginRoutes: Routes = [
    {path: CONSTANTS.LOCAL_ENDPOINTS.LOGIN, component: LoginComponent}
];
