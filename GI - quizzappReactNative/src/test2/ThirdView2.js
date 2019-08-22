import React from 'react';
import {Image, StyleSheet} from 'react-native';
import { Actions } from 'react-native-router-flux';
import {Container, Content, Text, Footer, Button} from 'native-base';
import styles from "../Styles";
import viewStore from "../ViewStore"

export default class ThirdView2 extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Image style={{maxHeight: 280, maxWidth: '95%', marginLeft: 10}} source={require('QuizApp/assets/polskaHipsometryczna.jpg')}/>
                    <Text style={styles.headerText}>Mapa hipsometryczna</Text>
                    <Text>Mapa hipsometryczna, mapa warstwobarwna[1] – mapa ogólnogeograficzna,
                        na której przy pomocy poziomic i barw między nimi zawartych jest odwzorowane ukształtowanie powierzchni lądu.
                        Mapa hipsometryczna przedstawia wybrane wysokości oraz plastycznie obrazuje układ nizin, wyżyn i gór.
                        Na szczegółowych mapach hipsometrycznych (małych terenów) można odczytać wypukłe formy terenu (pagórki, wzgórza i góry),
                        oraz formy wklęsłe (doliny i nawet małe kotliny).</Text>
                </Content>
                <Footer style={styles.footerFlex}>
                    <Button light style={styles.bigButton} onPress={()=> Actions.push(viewStore.getNextView("ThirdView"))}><Text style={styles.bigButtonText}>Następny</Text></Button>
                </Footer>
            </Container>
        );
    }
}