import {Routes, RouterModule} from "@angular/router";
import {LoginRoutes} from "./login/login.routes";
import {MainComponentsRoutes} from "./mainComponents/MainComponents.routes";

const appRoutes: Routes = [
    {path: '', redirectTo: '/login', pathMatch: 'full'},
    ...LoginRoutes,
    ...MainComponentsRoutes
];

export const appRoutingProviders: any[] = [];

export const routing = RouterModule.forRoot(appRoutes);
