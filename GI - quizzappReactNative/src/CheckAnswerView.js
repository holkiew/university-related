import React from 'react';
import {Image, StyleSheet, Dimensions, View} from "react-native";
import {Button, Container, Content, Text, Footer} from 'native-base';
import styles from "./Styles";
import TimerMixin from 'react-timer-mixin';
import {Actions} from "react-native-router-flux";
import Questions from "./questions"
import viewStore from "./ViewStore"
import PropTypes from 'prop-types';
import TestView from "./TestView";

export default class CheckAnswerView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            wrongAnswerExists : false,
            avoidRerenderFromSetState : false,
        }
    }

    render(){
        return (
            <Container>
                <Content>
                    <Container style={innerStyle.container}>
                        <View style={innerStyle.viewTitle}><Text style={innerStyle.textTitle}>Odpowiedzi na pytania</Text></View>
                        <View style={innerStyle.textView}><Text style={innerStyle.textQuestion}>1. {Questions.testQ1[1][0]}: </Text>{this.checkAnswer(1) ? <Text style={innerStyle.textGood}>Prawidłowa</Text> : <Text style={innerStyle.textBad}>Błędna</Text>}</View>
                        <View style={innerStyle.textView}><Text style={innerStyle.textQuestion}>2. {Questions.testQ1[2][0]}: </Text>{this.checkAnswer(2) ? <Text style={innerStyle.textGood}>Prawidłowa</Text> : <Text style={innerStyle.textBad}>Błędna</Text>}</View>
                        <View style={innerStyle.textView}><Text style={innerStyle.textQuestion}>3. {Questions.testQ1[3][0]}: </Text>{this.checkAnswer(3) ? <Text style={innerStyle.textGood}>Prawidłowa</Text> : <Text style={innerStyle.textBad}>Błędna</Text>}</View>
                        <View style={innerStyle.textView}><Text style={innerStyle.textQuestion}>4. {Questions.testQ1[4][0]}: </Text>{this.checkAnswer(4) ? <Text style={innerStyle.textGood}>Prawidłowa</Text> : <Text style={innerStyle.textBad}>Błędna</Text>}</View>
                        <View style={innerStyle.textView}><Text style={innerStyle.textQuestion}>5. {Questions.testQ1[5][0]}: </Text>{this.checkAnswer(5) ? <Text style={innerStyle.textGood}>Prawidłowa</Text> : <Text style={innerStyle.textBad}>Błędna</Text>}</View>
                    </Container>

                </Content>
                {!viewStore.secondTry && this.state.wrongAnswerExists?
                    <Footer>
                        <Button light warning style={innerStyle.bigButton} onPress={() => {Actions.reset(viewStore.displayView[0]); viewStore.secondTry = true}}><Text>Istnieje błędna odpowiedź, zacznij od nowa naukę</Text></Button>
                    </Footer>
                    : <View></View>}
            </Container>
        )
    }

    checkAnswer(questionNumber) {
        let rightAnswerNum = Questions.testQ1[questionNumber][6];

        let userAnswer = this.props.questionAnswers === undefined ? undefined : this.props.questionAnswers[questionNumber];
        let isRight = rightAnswerNum === userAnswer;

        if(!this.state.avoidRerenderFromSetState){
        if(!isRight) {
            let oldView = viewStore.displayView[questionNumber - 1];
            if (!oldView.includes("2")) {
                let newView = oldView + "2";
                viewStore.setView(oldView, newView);
                if (!this.state.wrongAnswerExists) {
                    this.setState({wrongAnswerExists: true, avoidRerenderFromSetState: true})
                }
            }
        }

            console.info("ViewStore, ", viewStore.displayView)
        }
        return isRight;
    }
}

TestView.propTypes = {
    questionAnswers : PropTypes.object,
};

const innerStyle = StyleSheet.create({
    container: {
        marginTop: 15,
        marginRight: 15,
        marginLeft: 15,
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    viewTitle:{
        flexBasis: '100%',
        alignItems: 'center'
    },
    textTitle:{
        fontSize: 20
    },
    textView:{
        marginTop: 5,
        marginBottom: 5,
        flexDirection: 'row',
        flexWrap: 'wrap',
        alignItems: 'center'
    },
    textQuestion:{
        flexBasis: '75%'
    },
    textGood: {
        fontSize: 18,
        color: "green",
        flexBasis: '25%'
    },
    textBad: {
        fontSize: 18,
        color: "red",
        flexBasis: '25%'
    },
    bigButton: {
        justifyContent: 'center',
        flexGrow: 1,
        height: '100%',
    }
});