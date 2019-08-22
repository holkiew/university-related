import {Component} from "react";
import React from "react";

export default class RoutesLineList extends Component {
    render() {
        return (
            <div className="connections-list">
                <div className="connection-item">
                    <div className="departure-time col-xs-2">10:21</div>
                    <div className="connection-info col-xs-8">
                        <div className="connections-numbers">
                            <div className="vehicle col-xs-5 no-padding">
                                <div className="vehicle-icon vehicle-tram-icon col-xs-6"></div>
                                <div className="vehicle-number-icon col-xs-offset-2 col-xs-4">33</div>
                            </div>
                            <div className="vehicle col-xs-5 no-padding">
                                <div className="vehicle-icon vehicle-bus-icon col-xs-6"></div>
                                <div className="vehicle-number-icon col-xs-offset-2 col-xs-4">3</div>
                            </div>
                            <div className="vehicle col-xs-2 no-padding">
                                <div className="vehicle-number-icon">...</div>
                            </div>
                        </div>
                        <div className="duration">33 minutes</div>
                    </div>
                    <div className="arrival-time col-xs-2">10:54</div>
                </div>
            </div>
        );
    }
}
