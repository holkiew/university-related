import React, {Component} from "react";
import NavigationMenuFindRoute from "./menu/NavigationMenuFindRoute";
import NavigationHeader from "./menu/NavigationHeader"


import NavigationMenuSchedules from "./menu/NavigationMenuSchedules";

const NAV_STATE = {ROUTES: 1, SCHEDULE: 2};

export default class NavigationPanel extends Component {
    state = {
        navState: NAV_STATE.ROUTES
    };

    render() {
        return (
            <section id="sidenav" className="col-lg-3 col-md-4 col-sm-6 col-xs-12 no-padding">
                <NavigationHeader state={this.state.navState} onNavStateChangeAction={this.changeNavState.bind(this)}/>
                {this.state.navState === NAV_STATE.ROUTES ?
                    <NavigationMenuFindRoute googleMapRef={this.props.googleMapRef}/> :
                    <NavigationMenuSchedules googleMapRef={this.props.googleMapRef}/>}
            </section>
        )
    }

    changeNavState(clickedStatePanel) {
        if (clickedStatePanel !== this.state.navState) {
            this.setState({
                navState: this.state.navState === NAV_STATE.ROUTES ? NAV_STATE.SCHEDULE : NAV_STATE.ROUTES
            })
        }
    }
}