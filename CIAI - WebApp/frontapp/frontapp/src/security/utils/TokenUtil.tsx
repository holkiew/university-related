import * as JWT from 'jwt-decode';
import * as env from "config.json";
import ROLES from "./Roles";
import {updateAxiosHeaderToken} from "configuration";

const EMPTY_TOKEN: string = "";

export interface TokenDTO {
    exp: number
    iat: number
    roles: []
    sub: string // userID
}

export interface RoleDTO {
    authority: string
}

export function setToken(tokenValue: string) {
    localStorage.setItem(env.security.localStorage.token, tokenValue);
    updateAxiosHeaderToken();
}

export function removeToken() {
    localStorage.setItem(env.security.localStorage.token, EMPTY_TOKEN);
    updateAxiosHeaderToken();
}

export function getToken(): string | null {
    const token: string | null = localStorage.getItem(env.security.localStorage.token);
    return token !== EMPTY_TOKEN ? token : null;
}

export function getRequestHeaderToken(): string | null {
    const token = getToken();
    return token !== null ? `Bearer ${token}` : null;
}

export function isTokenStored(): boolean {
    return getToken() !== null;
}

export function decodedToken(): TokenDTO | null {
    const token: string | null = getToken();
    return token ? JWT(token) : null;
}

export function hasRole(role: ROLES): boolean {
    const tokenDTO: TokenDTO | null = decodedToken();
    if (tokenDTO) {
        return tokenDTO.roles.find((value: RoleDTO)  => value.authority === role) !== undefined;
    } else {
        return false;
    }
}