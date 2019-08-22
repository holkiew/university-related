import React from 'react';
import styles from '../Styles'
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import {StyleSheet} from "react-native";
import {View, Image} from "react-native";
import viewStore from "../ViewStore";

export default class FourthView2 extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Samochody terenowe</Text>
                    <Container style={innerStyle.container}>
                        <View style={innerStyle.cellView}>
                            <Text>
                                jest to pojazd samochodowy przystosowany do pokonywania przeszkód terenowych.
                                Może być samochodem osobowym (łazik, samochód osobowo-terenowy) lub ciężarowym.

                            </Text>
                        </View>
                        <View style={innerStyle.cellView}>
                            <Image style={innerStyle.polishMapImage} source={require('QuizApp/assets/suzukisamuraj.jpg')}/>
                            <Text style={styles.caption}>Suzuki Samurai</Text>
                        </View>
                        <View>
                            <Text>
                                Zazwyczaj jest zbudowany w oparciu o ramę z napędem na wszystkie koła, specjalnymi oponami, skrzynią rozdzielczo-redukcyjną,
                                zwiększonym prześwitem poprzecznym (190 - 400 mm) i zwiększoną zdolnością pokonywania wzniesień
                            </Text>
                        </View>
                    </Container>
                    <View>
                        <Image style={innerStyle.worldMapImage} source={require('QuizApp/assets/pegperego.jpg')}/>
                        <Text style={styles.caption}>Peg Perego</Text>
                    </View>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("FourthView"))}><Text style={styles.bigButtonText}>Następny</Text></Button>
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
