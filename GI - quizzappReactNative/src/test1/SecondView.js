import React from 'react';
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import {Image, View, WebView} from "react-native";
import {StyleSheet} from "react-native";
import viewStore from "../ViewStore";

export default class SecondView extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Sprzęt wspinaczkowy</Text>
                    <View style={{height: 200}}>
                        <WebView
                            javaScriptEnabled={true}
                            domStorageEnabled={true}
                            source={{uri: 'https://www.youtube.com/embed/dFKhWe2bBkM'}}
                        />
                    </View>
                    <Container style={innerStyle.container}>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/eight.jpg')}/>
                            <Text style={styles.caption}>Eight</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/ekspres.jpg')}/>
                            <Text style={styles.caption}>Ekspres</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/fiveten.jpg')}/>
                            <Text style={styles.caption}>Fiveten</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/hms.jpg')}/>
                            <Text style={styles.caption}>HMS</Text>
                        </View>
                    </Container>
                </Content>
                <Footer style={styles.footerFlex}>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("SecondView"))}>
                        <Text style={styles.bigButtonText}>Następny</Text></Button>
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
        display: 'flex',
        flexDirection: 'row',
        flexWrap: 'wrap',
        height: '100%',
        justifyContent: 'space-around',
    },
    imageSize: {
        height: 150,
        width: 150
    },
    imageView: {
        width: '50%'
    }
});