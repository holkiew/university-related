import * as React from 'react';
import {Redirect, Route} from "react-router-dom";
import {security} from "config.json";
import {isTokenStored} from "../utils/TokenUtil";

const PrivateRoute = ({component: Component, ...rest}: any) => (
    <Route {...rest} render={(props) => (
        isTokenStored() ?
            <Component {...props}/> : <Redirect to='/login'/>
    )}/>
);

export default PrivateRoute;
