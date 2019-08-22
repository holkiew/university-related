import React from 'react';
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import {Image, View, WebView} from "react-native";
import {StyleSheet} from "react-native";
import viewStore from "../ViewStore";

export default class FirstView2 extends React.Component {
    render() {
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Tre Cime di Lavaredo</Text>
                    <View style={{height: 200}}>
                        <WebView
                            javaScriptEnabled={true}
                            domStorageEnabled={true}
                            source={{uri: 'https://www.youtube.com/embed/M2WHQkodMCk'}}
                        />
                    </View>
                    <Container style={innerStyle.container}>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/cimedi2.jpg')}/>
                            <Text style={styles.caption}>Widok góry 1</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/cimedi3.jpg')}/>
                            <Text style={styles.caption}>Widok góry 2</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/cimedi4.jpg')}/>
                            <Text style={styles.caption}>Widok góry 3</Text>
                        </View>
                        <View>
                            <Image style={innerStyle.imageSize} source={require('QuizApp/assets/cimedi5.jpg')}/>
                            <Text style={styles.caption}>Widok góry 4</Text>
                        </View>
                    </Container>
                </Content>
                <Footer style={styles.footerFlex}>
                    <Button light style={styles.bigButton} onPress={() => Actions.push(viewStore.getNextView("FirstView"))}>
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