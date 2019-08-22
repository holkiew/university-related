import React from 'react';
import {Button, Container, Content, Footer, List, ListItem, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import {Image, StyleSheet, View} from "react-native";
import Questions from "../questions";
import viewStore from "../ViewStore";

export default class SecondView2 extends React.Component {

    items = ['1. PROSTY PŁASKI', '2. Węzeł ŻEGLARSKI CUMOWY', '3. ÓSEMKA', '4. Węzły żeglarskie – SZOTOWY', '5. Węzeł HUNTERA',
        '6. Węzeł ratowniczy (skrajny tatrzański)', '7. Wyblinka'];

    render() {

        return (
            this.renderQuizView()
        );
    }


    renderQuizView(){
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Sprzęt wspinaczkowy i rodzaje wiązań</Text>
                    <View style={innerStyle.viewStyle}>
                        <Image style={innerStyle.images} source={require('QuizApp/assets/wezly_03.gif')}/>
                        <Text style={styles.caption}>Wiązanie tatrzańskie</Text>
                    </View>
                    <List dataArray={this.items} renderRow={(item) =>
                        <ListItem>
                            <Text>{item}</Text>
                        </ListItem>
                    }>
                    </List>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("SecondView"))}>
                        <Text style={styles.bigButtonText}>Następny</Text></Button>
                </Footer>
            </Container>
        )
    }
}


const innerStyle = StyleSheet.create({
        images: {
            maxHeight: 280,
            maxWidth: '95%',
            marginLeft: 10
        },
        viewStyle: {
            height: 300
        },
        buttonContainer: {
            marginTop: "50%",
            flexDirection: 'row',
            flexWrap: 'wrap',
            alignItems: 'center',
            alignContent: 'center',
            justifyContent: 'space-around'
        },

        buttonStyle: {
            backgroundColor: "orange",
            flexBasis: "40%"
        },
        buttonText: {
            color: 'white'
        }

    }
);