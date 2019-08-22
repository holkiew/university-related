import {Component} from "react";
import React from "react";

export default class NavigationHeader extends Component {

    render() {
        let img1Active = this.props.state === 1 ? "active" : "";
        let img2Active = this.props.state === 2 ? "active" : "";
        return (
            <header className="header col-xs-12 no-padding">
                <a href="./" className="col-xs-3 no-padding">
                    <img alt="logo" className="logo" src="./images/logo.png"/>
                </a>
                <div className="section-tabs col-xs-9 no-padding">
                    <div id="route-tab" onClick={() => this.props.onNavStateChangeAction(1)}
                         className={`section-tab ${img1Active} col-xs-6`}>
                        <i className="icon fas fa-route"></i>
                        <div>Find route</div>
                    </div>
                    <div id="schedules-tab" onClick={() => this.props.onNavStateChangeAction(2)}
                         className={`section-tab ${img2Active} col-xs-6`}>
                        <i className="icon far fa-calendar-alt"></i>
                        <div>Schedules</div>
                    </div>
                </div>
            </header>
        );
    }
}