import axios from "axios";
import * as env from "config.json";
import {getRequestHeaderToken} from "./security/utils/TokenUtil";

export function configureAxiosInstances() {
    axios.defaults.baseURL = env.backendServer.baseUrl;
    axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
    updateAxiosHeaderToken();
}

export function updateAxiosHeaderToken() {
    axios.defaults.headers.common.Authorization = getRequestHeaderToken();
}