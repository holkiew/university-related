import {Component} from "@angular/core";

declare let google: any;

@Component({
    moduleId: module.id,
    selector: 'as-direction-map',
    templateUrl: 'directionMap.html',
    styleUrls: ['directionMap.css']
})

export class DirectionMapComponent {
    ngOnInit() {
        let mapProp = {
            center: new google.maps.LatLng(51.097139, 17.010456),
            zoom: 5,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        let map = new google.maps.Map(document.getElementById("map"), mapProp);
        let marker = new google.maps.Marker({
            position: {lat: 51.097139, lng: 17.010456},
            map: map,
            draggable: false
        });
    }
}
