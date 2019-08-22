import React from 'react';
import { Router, Scene } from 'react-native-router-flux';
import {FirstView, SecondView, ThirdView, FourthView, FifthView} from './test1/TestViews';
import {FirstView2, SecondView2, ThirdView2, FourthView2, FifthView2} from './test2/TestViews2';
import TestView from "./TestView";
import CheckAnswerView from "./CheckAnswerView"

const Routes = () => (
    <Router>
        <Scene key = "root">
            <Scene key = "FirstView" component = {FirstView} title = "Tre Cime di Lavaredo"   initial = {true} />

            <Scene key = "SecondView" component = {SecondView} title = "Sprzęt wspinaczkowy i rodzaje wiązań"/>
            <Scene key = "ThirdView" component = {ThirdView} title = "Mapa hipsometryczna"/>
            <Scene key = "FourthView" component = {FourthView} title = "Samochody terenowe" />
            <Scene key = "FifthView" component = {FifthView} title = "Budowa wulkanu"  />


            <Scene key = "FirstView2" component = {FirstView2} title = "Tre Cime di Lavaredo"  />
            <Scene key = "SecondView2" component = {SecondView2} title = "Sprzęt wspinaczkowy i rodzaje wiązań" />
            <Scene key = "ThirdView2" component = {ThirdView2} title = "Mapa hipsometryczna"/>
            <Scene key = "FourthView2" component = {FourthView2} title = "Samochody terenowe" />
            <Scene key = "FifthView2" component = {FifthView2} title = "Budowa wulkanu"/>

            <Scene key = "TestView" component = {TestView} title = "TEST" />
            <Scene key = "CheckAnswerView" component = {CheckAnswerView} title = "Rezultat"/>
        </Scene>
    </Router>
);

export default Routes