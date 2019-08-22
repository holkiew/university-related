import {Component} from "react";
import React from "react";

const SCHEDULE_BUTTONS = {MO_FR: 1, SA: 2, SU_HO: 3};

export default class SchedulesLineSchedule extends Component {
    state = {
        clickedButton: SCHEDULE_BUTTONS.MO_FR
    };

    render() {
        return (
            <div className="connection-schedule">
                <div className="connection-header col-xs-12">
                    {
                        this.props.onBackButtonClick ? (
                            <div className="back-button btn btn-default pull-left"
                                 onClick={this.props.onBackButtonClick}>
                                <i className="fas fa-chevron-left"></i> Back
                            </div>) : ""
                    }
                    <div className={this.props.onBackButtonClick ? "connection-info pull-right" : "connection-info"}>
                        <div className="station-name col-xs-6" title={this.props.stationName}>{this.props.stationName}</div>
                        <div className="vehicle col-xs-6">
                            <div className="vehicle-number-icon col-xs-6">{this.props.lineName}</div>
                            <div className="vehicle-icon vehicle-tram-icon col-xs-4"></div>
                        </div>
                    </div>
                </div>
                <div className="col-xs-12">
                    <table className="schedule-table table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th></th>
                            <th className="schedule-days"><span
                                className={`btn ${this.state.clickedButton === SCHEDULE_BUTTONS.MO_FR ? "btn-success" : "btn-default"} col-xs-12`}
                                onClick={() => this.setState({clickedButton: SCHEDULE_BUTTONS.MO_FR})}>Mo-Fr</span>
                            </th>
                            <th className="schedule-days">
                                <span
                                    className={`btn ${this.state.clickedButton === SCHEDULE_BUTTONS.SA ? "btn-success" : "btn-default"} col-xs-12`}
                                    onClick={() => this.setState({clickedButton: SCHEDULE_BUTTONS.SA})}>Sa</span>
                            </th>
                            <th colSpan="2" className="schedule-days">
                                <span
                                    className={`btn ${this.state.clickedButton === SCHEDULE_BUTTONS.SU_HO ? "btn-success" : "btn-default"} col-xs-12`}
                                    onClick={() => this.setState({clickedButton: SCHEDULE_BUTTONS.SU_HO})}>Su + Ho</span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td className="hour">07</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">08</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">09</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">10</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">11</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">12</td>
                            <td className="minutes" colSpan="3">
                                <span>00</span>
                                <span>05</span>
                                <span>12</span>
                                <span>21</span>
                                <span>42</span>
                            </td>
                        </tr>
                        <tr>
                            <td className="hour">13</td>
                            <td className="minutes" colSpan="3">
                                <span>00</span>
                                <span>05</span>
                                <span>12</span>
                                <span>21</span>
                                <span>42</span>
                            </td>
                        </tr>
                        <tr>
                            <td className="hour">14</td>
                            <td className="minutes" colSpan="3">
                                <span>00</span>
                                <span>05</span>
                                <span>12</span>
                                <span>21</span>
                                <span>42</span>
                            </td>
                        </tr>
                        <tr>
                            <td className="hour">15</td>
                            <td className="minutes" colSpan="3">
                                <span>00</span>
                                <span>05</span>
                                <span>12</span>
                                <span>21</span>
                                <span>42</span>
                            </td>
                        </tr>
                        <tr>
                            <td className="hour">16</td>
                            <td className="minutes" colSpan="3">
                                <span>00</span>
                                <span>05</span>
                                <span>12</span>
                                <span>21</span>
                                <span>42</span>
                            </td>
                        </tr>
                        <tr>
                            <td className="hour">17</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">18</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">19</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        <tr>
                            <td className="hour">20</td>
                            <td className="minutes" colSpan="3"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}
