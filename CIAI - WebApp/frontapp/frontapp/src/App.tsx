import * as React from 'react';
import Routes from "./Routes";
import {configureAxiosInstances} from "./configuration";

export default class App extends React.Component {

    public componentWillMount() {
        configureAxiosInstances();
    }
    public render() {
        return (
            <Routes/>
        );
    }

}
