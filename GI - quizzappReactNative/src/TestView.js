import React from 'react';
import {Image, StyleSheet, Dimensions, View} from "react-native";
import {Button, Container, Content, Text, Footer} from 'native-base';
import styles from "./Styles";
import TimerMixin from 'react-timer-mixin';
import {Actions} from "react-native-router-flux";
import PropTypes from 'prop-types';

export default class TestView extends React.Component {
    buttons = [];

    constructor(props) {
        super(props);
        this.state = {
            stage: 1,
            won: false,
            userAnswerButton: 0,
            questionAnswers: {}
        }

    }
    render() {

        return (
            <Container>
                {!this.state.won ? (
                    <Content>
                        <Text style={styles.headerText}>{this.props.questions[this.state.stage][0]}</Text>
                        <Container style={innerStyle.container}>
                            {
                                this.arrayRange(1,6).map((k,v) => this.createButton(k))
                            }
                        </Container>
                    </Content>
                ) : (
                    <Content>
                        <Image style={innerStyle.winImage} source={require('QuizApp/assets/youwon.png')}/>
                    </Content>

                )}
                <Footer>
                    {this.state.stage > 1 ? <Button light style={styles.bigButton} onPress={() => this.moveQuestion(-1)}><Text style={styles.bigButtonText}>Poprzednie pytanie</Text></Button> : <View></View>}
                    {this.state.stage < 5 ? <Button light style={styles.bigButton} onPress={() => this.moveQuestion(1)}><Text style={styles.bigButtonText}>Następne pytanie</Text></Button>
                        : <Button light style={styles.bigButton} onPress={() => Actions.reset("CheckAnswerView", {questionAnswers: this.state.questionAnswers})}><Text style={styles.bigButtonText}>Sprawdź odpowiedzi</Text></Button>}
                </Footer>
            </Container>
        )
    }


    moveQuestion(stageAddition){
        let userMarkedButton = this.state.questionAnswers[this.state.stage + stageAddition]
        this.setState({
            userAnswerButton: userMarkedButton === undefined ? 0 : userMarkedButton,
            stage: this.state.stage + stageAddition
        });
    }

    createButton(order) {
        return (
            <Button block warning style={[innerStyle.containerItem, this.state.userAnswerButton == order ? {backgroundColor: '#57FF24'} : {}]}
                        ref={node => this.buttons[order] = node} key={order} onPress={(event) => this.markAnswer(event, order)}>
            <Text>{this.props.questions[this.state.stage][order]}</Text>
        </Button>);
    }

    markAnswer(event, buttonNumber) {
        this.setState({userAnswerButton: buttonNumber});
        this.state.questionAnswers[this.state.stage] = buttonNumber;
    }

    arrayRange(start, end){
        return Array.from({length: (end - start)}, (v, k) => k + start);
    }

}
//nie dziala walidacja, ale IntelliJ wykrywa typy po nacisnieciu ctrl
TestView.propTypes = {
    questions : PropTypes.object,
    testVersion: PropTypes.number
};

const innerStyle = StyleSheet.create({
    container: {
        marginTop: 15,
        display: 'flex',
        alignItems: 'center',
    },
    containerItem: {
        margin: 30
    },
    images: {
        maxHeight: 280,
        maxWidth: '95%',
        marginLeft: 10
    },
    winImage: {
        width: "100%",
        maxHeight: Dimensions.get("window").height
    }
});