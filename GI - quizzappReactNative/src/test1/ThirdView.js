import React from 'react';
import styles from '../Styles'
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import {StyleSheet} from "react-native";
import {View, Image} from "react-native";
import viewStore from "../ViewStore";

export default class ThirdView extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Mapa hipsometryczna</Text>
                    <Container style={innerStyle.container}>
                        <View style={innerStyle.cellView}>
                            <Text>
                                Mapa ogólnogeograficzna, na której przy pomocy poziomic i barw między nimi zawartych jest odwzorowane ukształtowanie powierzchni lądu.
                            </Text>
                        </View>
                        <View style={innerStyle.cellView}>
                            <Image style={innerStyle.polishMapImage} source={require('QuizApp/assets/hipsometricMap.jpg')}/>
                            <Text style={styles.caption}>Polska mapa fizyczna</Text>
                        </View>
                        <View>
                            <Text>
                                Mapa hipsometryczna przedstawia wybrane wysokości oraz plastycznie obrazuje układ nizin, wyżyn i gór.
                                Na szczegółowych mapach hipsometrycznych (małych terenów) można odczytać wypukłe formy terenu (pagórki, wzgórza i góry), oraz formy wklęsłe (doliny i nawet małe kotliny).
                            </Text>
                        </View>
                    </Container>
                    <View>
                        <Image style={innerStyle.worldMapImage} source={require('QuizApp/assets/worldHipsometricMap.jpg')}/>
                        <Text style={styles.caption}>Swiatowa mapa fizyczna</Text>
                    </View>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("ThirdView"))}><Text style={styles.bigButtonText}>Następny</Text></Button>
                </Footer>
            </Container>
        );
    }
}

const innerStyle = StyleSheet.create({
    container: {
        marginTop: 15,
        marginRight: 15,
        marginLeft: 15,
        marginBottom: 65,
        display: 'flex',
        flexDirection: 'row',
        flexWrap: 'wrap',
        height: 'auto',
        justifyContent: 'space-around',
    },
    cellView: {
        flexBasis: '50%'
    },
    polishMapImage: {
        height: '70%',
        width: 'auto'
    },
    worldMapImage: {
        height: 250,
        width: 'auto'
    }
});
