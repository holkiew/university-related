import React, {Component} from "react";
import RoutesEmpty from "./results/routes/RoutesEmpty";
import RoutesLineList from "./results/routes/RoutesLineList";
import RouteDetailedInfo from "./results/routes/RouteDetailedInfo";
import Autosuggest from 'react-autosuggest';
import inputSuggestions from "./input_suggestions";
const MENU_STATE = {EMPTY: 1, SEARCH: 2, ROUTE_DETAILED_INFO: 3};

export default class NavigationMenuRoutes extends Component {
    state = {
        menuState: MENU_STATE.EMPTY,
        startStationInput: "",
        startStationInputSuggestions: inputSuggestions.findRouteSuggestions,
        destinationStationInput: "",
        destinationStationInputSuggestions: inputSuggestions.findRouteSuggestions
    };

    render() {
        return (
            <nav>
                <div className="menu menu-route col-xs-12">
                    <div className="options">
                        <div className="col-xs-11">
                            <div className="col-xs-12 no-padding form-group">
                                <label className="col-xs-2 point col-form-label">A</label>
                                <div className=" col-xs-offset-1 col-xs-10">
                                    <Autosuggest
                                        suggestions={this.state.startStationInputSuggestions}
                                        onSuggestionsFetchRequested={(v) => {
                                        }}
                                        onSuggestionsClearRequested={(v) => {
                                        }}
                                        getSuggestionValue={(suggestion) => suggestion.name}
                                        renderSuggestion={(suggestion) => <p>{suggestion.name}</p>}
                                        inputProps={{
                                            placeholder: 'Starting point',
                                            value: this.state.startStationInput,
                                            onChange: (event, {newValue}) => this.setState({startStationInput: newValue})
                                        }}/>
                                </div>
                            </div>
                            <div className="col-xs-12 no-padding form-group">
                                <label className="col-xs-2 point col-form-label">B</label>
                                <div className=" col-xs-offset-1 col-xs-10">
                                    <Autosuggest
                                        suggestions={this.state.destinationStationInputSuggestions}
                                        onSuggestionsFetchRequested={(v) => {
                                        }}
                                        onSuggestionsClearRequested={(v) => {
                                        }}
                                        getSuggestionValue={(suggestion) => suggestion.name}
                                        renderSuggestion={(suggestion) => <p>{suggestion.name}</p>}
                                        inputProps={{
                                            placeholder: 'Destination point',
                                            value: this.state.destinationStationInput,
                                            onChange: (event, {newValue}) => this.setState({destinationStationInput: newValue})
                                        }}/>
                                </div>
                            </div>
                        </div>
                        <div id="switch-stations-btn" type="button" className="col-xs-1 btn btn-default"
                             onClick={this.switchStationsOnClick.bind(this)} title="Switch route points">
                            <img alt="switch img" className="switch-img" src="./images/sort.png"/>
                        </div>
                    </div>
                    <div className="advanced-options col-xs-12 form-group">
                        <div className="col-md-4 col-xs-12"><input className="form-control" type="text" value="10:12"/>
                        </div>
                        <div className="col-md-4 col-xs-12"><input className="form-control" type="text"
                                                                   value="11/10/18"/></div>
                        <div className="col-md-4 col-xs-12"><input className="form-control" type="text"
                                                                   value="Departure"/></div>
                    </div>
                    <div className="col-xs-12">
                        <input id="search-route-btn" type="submit" value="SEARCH"
                               className="col-xs-11 btn btn-success"
                               onClick={this.onSearchClick.bind(this)}/>
                        <div className="col-xs-1">
                            <div id="options-btn" className="btn btn-default" title="Options">
                                <img alt="options-img" className="options-img" src="./images/settings.png"/>
                            </div>
                            <div id="options-btn" class="colapse btn btn-default" title="Options">
                                <span className="options-img glyphicon glyphicon-chevron-up"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <section id="results" className="col-xs-12 no-padding">
                    {
                        (() => {
                            switch (this.state.menuState) {
                                case MENU_STATE.EMPTY:
                                    return <RoutesEmpty/>;
                                case MENU_STATE.SEARCH:
                                    return (
                                        <div onClick={() => this.setState({menuState: MENU_STATE.ROUTE_DETAILED_INFO})}>
                                            <RoutesLineList/>
                                        </div>
                                    );
                                case MENU_STATE.ROUTE_DETAILED_INFO:
                                    return <RouteDetailedInfo
                                        onBackButtonClick={() => this.setState({menuState: MENU_STATE.SEARCH})}/>;
                                default:
                                    return "STMH NOT RIGHT";
                            }
                        })()
                    }
                </section>
            </nav>
        )
    }

    onSearchClick() {
        this.setState({menuState: MENU_STATE.SEARCH});
        let address1 = inputSuggestions.findRouteSuggestions.find((suggestion) => suggestion.name === this.state.destinationStationInput);
        let address2 = inputSuggestions.findRouteSuggestions.find((suggestion) => suggestion.name === this.state.startStationInput);
        if (address1 && address2) {
            this.props.googleMapRef.current.setNewLocationFromAddress(address1.value, address2.value);
        }
    }

    switchStationsOnClick() {
        let newStart = this.state.destinationStationInput;
        let newDestination = this.state.startStationInput;
        this.setState({
            startStationInput: newStart,
            destinationStationInput: newDestination
        });
        let address = inputSuggestions.findRouteSuggestions.find((suggestion) => suggestion.name === newStart);
        if (address) {
            this.props.googleMapRef.current.setNewLocationFromAddress(address.value);
        }
    }
}