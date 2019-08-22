import React, {Component} from "react";
import SchedulesEmpty from "./results/schedules/SchedulesEmpty";
import SchedulesLineSchedule from "./results/schedules/SchedulesLineSchedule";
import SchedulesStopDetails from "./results/schedules/SchedulesStopDetails";
import SchedulesStopInfo from "./results/schedules/SchedulesStopInfo";
import Autosuggest from "react-autosuggest";
import inputSuggestions from "./input_suggestions";

const MENU_STATE = {
    EMPTY: 1,
    SEARCH_STATION: 2,
    SEARCH_LINE: 3,
    LINE_STATION_SCHEDULE: 4
};

export default class NavigationMenuSchedules extends Component {

    state = {
        menuState: MENU_STATE.EMPTY,
        prevMenuState: 0,
        stationOrLineNumberInput: "",
        stationOrLineNumberInputSuggestions: inputSuggestions.schedulesSuggestions
    };

    render() {
        return (
            <nav>
                <div className="menu menu-schedules col-xs-12">
                    <div className="col-xs-12 form-group">
                        <Autosuggest
                            suggestions={this.state.stationOrLineNumberInputSuggestions}
                            onSuggestionsFetchRequested={(v) => {
                            }}
                            onSuggestionsClearRequested={(v) => {
                            }}
                            getSuggestionValue={(suggestion) => suggestion.name}
                            renderSuggestion={(suggestion) => <p>{suggestion.name}</p>}
                            inputProps={{
                                placeholder: 'Station or line number',
                                value: this.state.stationOrLineNumberInput,
                                onChange: (event, {newValue}) => this.setState({stationOrLineNumberInput: newValue})
                            }}/>
                    </div>
                    <div className="col-xs-12 form-group">
                        <input id="search-schedules-btn" type="submit" value="SEARCH"
                               onClick={this.searchStationOnClick.bind(this)}
                               className="btn btn-success form-control"/>
                    </div>
                </div>
                {
                    (() => {
                        switch (this.state.menuState) {
                            case MENU_STATE.EMPTY:
                                return <SchedulesEmpty/>;
                            case MENU_STATE.SEARCH_STATION:
                                return <SchedulesStopInfo stationName={this.state.stationName}
                                                          onLineClick={this.schedulesStopInfoOnLineClick.bind(this)}/>;
                            case MENU_STATE.SEARCH_LINE:
                                return <SchedulesStopDetails lineName={this.state.lineName}
                                                             onStationClick={this.searchLineOnStationClick.bind(this)}/>;
                            case MENU_STATE.LINE_STATION_SCHEDULE:
                                return <SchedulesLineSchedule lineName={this.state.lineName}
                                                              stationName={this.state.stationName}
                                                              onBackButtonClick={this.lineScheduleBackButtonClick.bind(this)}/>;
                            default:
                                return "STMH NOT RIGHT";
                        }
                    })()
                }
            </nav>
        )
    }

    searchStationOnClick() {
        if (this.state.stationOrLineNumberInput.length > 0) {
            let isNumber = /^\d+$/.test(this.state.stationOrLineNumberInput);
            let newState = isNumber ? {
                menuState: MENU_STATE.SEARCH_LINE,
                lineName: this.state.stationOrLineNumberInput
            } : {
                menuState: MENU_STATE.SEARCH_STATION,
                stationName: this.state.stationOrLineNumberInput
            };
            this.setState(newState);

            let address = inputSuggestions.schedulesSuggestions.find((suggestion) => suggestion.name === this.state.stationOrLineNumberInput);
            if (address) {
                this.props.googleMapRef.current.setNewLocationFromAddress(address.value);
            }
        }
    }

    searchLineOnStationClick(stationName) {
        this.setState({
            menuState: MENU_STATE.LINE_STATION_SCHEDULE,
            menuPrevState: this.state.menuState,
            stationName: stationName
        })
    }


    lineScheduleBackButtonClick() {
        this.setState({
            menuState: this.state.menuPrevState
        })
    }

    schedulesStopInfoOnLineClick(stationName, lineName) {
        this.setState({
            menuState: MENU_STATE.LINE_STATION_SCHEDULE,
            menuPrevState: this.state.menuState,
            stationName: stationName,
            lineName: lineName
        })
    }
}