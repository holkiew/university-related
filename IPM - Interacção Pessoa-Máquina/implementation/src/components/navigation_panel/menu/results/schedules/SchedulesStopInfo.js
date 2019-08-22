import React, {Component} from "react";

export default class SchedulesStopInfo extends Component {

    state = {
        stopName: this.props.stationName
    };

    render() {
        return (
            <div className="stop-info">
                <div className="stop-numbers col-xs-12">
                    <div className="stop-number"
                         onClick={(e) => this.props.onLineClick(this.state.stopName, e.currentTarget.textContent)}>112
                    </div>
                    <div className="stop-number"
                         onClick={(e) => this.props.onLineClick(this.state.stopName, e.currentTarget.textContent)}>114
                    </div>
                    <div className="stop-number"
                         onClick={(e) => this.props.onLineClick(this.state.stopName, e.currentTarget.textContent)}>25
                    </div>
                    <div className="stop-number"
                         onClick={(e) => this.props.onLineClick(this.state.stopName, e.currentTarget.textContent)}>3
                    </div>
                    <div className="stop-number">...</div>
                </div>
                <div className="delimiter col-xs-12"></div>
                <div className="stop-departures col-xs-12 no-padding">
                    <div className="connection-item">
                        <div className="departure-time col-xs-2">10:21</div>
                        <div className="connection-info col-xs-4">
                            <div className="vehicle col-xs-12 no-padding">
                                <div className="vehicle-icon vehicle-tram-icon col-xs-6"></div>
                                <div className="vehicle-number-icon col-xs-offset-2 col-xs-4">3</div>
                            </div>
                        </div>
                        <div className="route-info col-xs-6">
                            <div>Universidade</div>
                            <span className="glyphicon glyphicon-arrow-down"></span>
                            <div>Cais do Sodré</div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
