import React, {Component} from 'react';
import GoogleMap from "./components/google_maps/GoogleMap";
import NavigationMenuFindRoute from "./components/navigation_panel/NavigationPanel";
import References from "./References";

class App extends Component {
    constructor(props) {
        super(props);
        this.googleMap = React.createRef();

    }

    componentDidMount() {
        console.info(this.googleMap.current)
    }

    render() {
        return (
            <body>
            <NavigationMenuFindRoute googleMapRef={this.googleMap}/>
            <section className="col-lg-9 col-md-8 col-sm-6 hidden-xs no-padding">
                <GoogleMap ref={this.googleMap}/>
            </section>
            <References/>
            </body>
        );
    }
}

export default App;
