import React, {Component} from 'react';
import GoogleMapReact from 'google-map-react';

const API_KEY = "AIzaSyDlWbnGkLtEp6On8lXp2mgBGUpAmDTdzHc";

class GoogleMap extends Component {
    constructor(props) {
        super(props);
        this.state = {
            center: {
                lat: 38.683617,
                lng: -9.177595
            },
            zoom: 13,
            markers: []
        }
    }

    render() {
        // Important! Always set the container height explicitly
        return (<div style={{height: '100vh', width: '100%'}}>
                <GoogleMapReact
                    key={this.state.center.lat}
                    bootstrapURLKeys={{key: API_KEY}}
                    defaultCenter={this.state.center}
                    defaultZoom={this.state.zoom}
                    onGoogleApiLoaded={({map, maps}) => this.initGoogleApi(map, maps)}
                    yesIWantToUseGoogleMapApiInternals
                >
                </GoogleMapReact>
            </div>
        );
    }

    initGoogleApi(map, maps) {
        this.geocoder = new maps.Geocoder();
        this.map = map;
        this.maps = maps;

    }

    addMarks(map, maps, markers) {
        markers.forEach(marker => {
            console.info(marker);
            new maps.Marker({
                position: marker.center,
                map,
                title: marker.name
            });
        })
    }

    setNewLocationFromAddress(address1, address2) {
        // if (address1 && address1.length > 0 && address2 && address2.length > 0) {
        //     this.twoAddressesProvided(address1, address2);
        // } else if (address1 && address1.length > 0) {
        //     this.oneAddressProvided(address1);
        // }
    }

    twoAddressesProvided(address1, address2) {
        this.geocoder.geocode({address: address1},
            (results, status) => {
                if (status === 'OK') {
                    const {location} = results[0].geometry;
                    let location1 = location;
                    let location2;
                    this.geocoder.geocode({address: address2},
                        (results, status) => {
                            if (status === 'OK') {
                                const {location} = results[0].geometry;
                                location2 = location;
                                let marks = [{center: location1, name: address1}, {center: location2, name: address2}];
                                this.setState({
                                    center: {
                                        lat: Math.abs(location1.lat() + location2.lat()) / 2,
                                        lng: Math.abs(location1.lng() + location2.lng()) / 2
                                    },
                                    markers: marks
                                });
                                this.addMarks(this.map, this.maps, marks);

                            } else {
                                console.warn('Geocode was not successful for the following reason: ' + status);
                                console.warn('Lisbon set');
                            }
                        });
                } else {
                    console.warn('Geocode was not successful for the following reason: ' + status);
                    console.warn('Lisbon set');
                }
            });
    }

    oneAddressProvided(address) {
        this.geocoder.geocode({address: address},
            (results, status) => {
                if (status === 'OK') {
                    const {location} = results[0].geometry;
                    let marks = [{center: location, name: address}];
                    this.setState({
                        center: {
                            lat: location.lat(),
                            lng: location.lng()
                        },
                        markers: marks
                    });
                    this.addMarks(this.map, this.maps, marks);

                } else {
                    console.warn('Geocode was not successful for the following reason: ' + status);
                    console.warn('Lisbon set');
                }
            });
    }
}

export default GoogleMap;