import React from 'react';
import {Image, StyleSheet} from 'react-native';
import { Actions } from 'react-native-router-flux';
import {Container, Content, Text, Footer, Button} from 'native-base';
import styles from "../Styles";
import viewStore from "../ViewStore"

export default class FirstView extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Image source={require('QuizApp/assets/Dolomiten_Drei_Zinnen.jpg')}/>
                    <Text style={styles.headerText}>Tre Cime di Lavaredo</Text>
                    <Text>(w języku włoskim trzy szczyty Lavaredo) - masyw górski w Dolomitach składający się z trzech głównych szczytów: Cima Ovest (wł. zachodni szczyt – 2973 m n.p.m.), Cima Grande (wł. wielki szczyt – 3003 m n.p.m.), Cima Piccola (wł. mały szczyt – 2857 m n.p.m.) oraz dwóch mniejszych turni: Piccolissima i Punta di Frida. Na szczyt prowadzą jedynie drogi wspinaczkowe.</Text>
                </Content>
                <Footer style={styles.footerFlex}>
                    <Button light style={styles.bigButton} onPress={()=> Actions.push(viewStore.getNextView("FirstView"))}><Text style={styles.bigButtonText}>Następny</Text></Button>
                </Footer>
            </Container>
        );
    }
}