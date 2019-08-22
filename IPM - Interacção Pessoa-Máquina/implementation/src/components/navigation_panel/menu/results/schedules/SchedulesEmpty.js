import {Component} from "react";
import React from "react";

export default class SchedulesEmpty extends Component {
    render() {
        return (
            <div className="no-result-schedule col-xs-12">
                <i className="far fa-calendar-alt"></i>
            </div>
        );
    }
}
