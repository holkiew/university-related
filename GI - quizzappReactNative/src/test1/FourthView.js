import React from 'react';
import {Image, StyleSheet, View} from 'react-native';
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import viewStore from "../ViewStore";

export default class FourthView extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Samochody terenowe</Text>
                    <Container style={innerStyle.container}>
                        <View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/honker.jpg')}/>
                                <Text style={styles.caption}>Honker</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/kiaasia.jpg')}/>
                                <Text style={styles.caption}>Kia Asia</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/pegperego.jpg')}/>
                                <Text style={styles.caption}>Peg Pegero</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/suzukisamuraj.jpg')}/>
                                <Text style={styles.caption}>Suzuki Samurai</Text>
                            </View>
                        </View>
                    </Container>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("FourthView"))}>
                        <Text style={styles.bigButtonText}>Następny</Text></Button>
                </Footer>
            </Container>
        );
    }
}

const innerStyle = StyleSheet.create({
    container: {
        marginTop: 15,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: '100%'
    },
    images: {
        maxHeight: 280,
        maxWidth: '95%',
        marginLeft: 10
    }
});