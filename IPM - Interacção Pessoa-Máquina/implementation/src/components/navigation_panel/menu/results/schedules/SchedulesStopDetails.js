import {Component} from "react";
import React from "react";

export default class SchedulesStopDetails extends Component {
    render() {
        return (
            <div className="connection-detail">
                <div className="connection-header col-xs-12">
                    {/*<div className="back-button btn btn-default pull-left"><i*/}
                    {/*className="fas fa-chevron-left"></i> Back*/}
                    {/*</div>*/}
                    {/*pull-right*/}
                    <div className="connection-info">
                        <div className="vehicle col-xs-3 pull-right">
                            <div className="vehicle-number-icon col-xs-6">{this.props.lineName}</div>
                            <div className="vehicle-icon vehicle-tram-icon col-xs-4"></div>
                        </div>
                    </div>
                </div>
                <div className="connection-route">
                    <div className="station end-station"
                         onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="fas fa-circle"></i> Universidade
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Monte de Caparica
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Fomega
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Boa Esperanca
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Pragal
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Rahalma
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Bento Goncalves
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Almada
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Sao Joao Baptista
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> Gil Vicente
                    </div>
                    <div className="station" onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="far fa-circle"></i> 25 de Abril
                    </div>
                    <div className="station end-station"
                         onClick={(e) => this.props.onStationClick(e.currentTarget.textContent)}><i
                        className="fas fa-flag-checkered"></i> Cacilhas
                    </div>
                </div>
            </div>
        );
    }
}
