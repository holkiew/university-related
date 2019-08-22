import {Component} from "react";
import React from "react";

export default class RouteDetailedInfo extends Component {
    render() {
        return (
            <div className="connection-graphic">
                <div className="connection-header col-xs-12">
                    <div onClick={this.props.onBackButtonClick}
                         className="back-button btn btn-default pull-left"><i
                        className="fas fa-chevron-left"></i> Back
                    </div>
                </div>
                <div className="route">
                    <div className="time">10:21</div>
                    <div className="station end-station"><i className="fas fa-circle"></i> Universidade</div>
                    <div className="vehicle col-xs-12">
                        <div className="vehicle-icon vehicle-tram-icon col-xs-2"></div>
                        <div className="vehicle-number-icon col-xs-2">3</div>
                    </div>
                    <div className="station"><i className="far fa-circle"></i> Cacilhas</div>
                    <div className="time">10:31</div>
                    <div className="walking"><i className="fas fa-walking icon"></i> 3 min</div>
                    <div className="time">10:45</div>
                    <div className="station"><i className="far fa-circle"></i> Cacilhas</div>
                    <div className="vehicle col-xs-12">
                        <div className="vehicle-icon vehicle-boat-icon col-xs-2"></div>
                        <div className="vehicle-number-icon col-xs-2">B2</div>
                    </div>
                    <div className="station end-station"><i className="fas fa-flag-checkered"></i> Cais do Sodré
                    </div>
                    <div className="time">10:54</div>
                </div>
            </div>
        );
    }
}
