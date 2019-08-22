import React from 'react';
import {Image, StyleSheet, View} from 'react-native';
import {Button, Container, Content, Footer, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import Questions from "../questions"

export default class FifthView2 extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            testView: false
        }
    }

    render() {
        return (
            this.state.testView ? this.renderToTestView() : this.renderQuizView()
        );
    }

    renderToTestView(){
        return(
            <Container>
                <Content>
                    <Container style={{height: '100%'}}>
                        <Container style={innerStyle.buttonContainer}>
                            <Button light style={innerStyle.buttonStyle} onPress={() => this.setState({testView: false})}>
                                <Text style={innerStyle.buttonText}>Chce się dalej uczyć</Text></Button>
                            <Button block light style={innerStyle.buttonStyle} onPress={() => Actions.reset("TestView", {questions: Questions.testQ1, testVersion: 1})}>
                                <Text style={innerStyle.buttonText}>Do testu</Text></Button>
                        </Container>
                    </Container>
                </Content>
            </Container>
        )
    }

    renderQuizView(){
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Budowa wulkanu</Text>
                    <Container style={innerStyle.container}>
                        <View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/przekrojwulkanu.jpg')}/>
                                <Text style={styles.caption}>Przekrój wulkanu</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/wulkan1.jpg')}/>
                                <Text style={styles.caption}>Wulkan podczas erupcji</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/wulkan3.jpg')}/>
                                <Text style={styles.caption}>Budowa wulkanu 1</Text>
                            </View>
                            <View>
                                <Image style={innerStyle.images} source={require('QuizApp/assets/wulkan4.jpg')}/>
                                <Text style={styles.caption}>Budowa Wulkanu 2</Text>
                            </View>
                        </View>
                    </Container>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => this.setState({testView: true})}>
                        <Text style={styles.bigButtonText}>Test</Text></Button>
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
        textAlign: 'center',
        color: 'white'
    }
});