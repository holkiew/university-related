import React from 'react';
import {Button, Container, Content, Footer, List, ListItem, Text} from 'native-base';
import {Actions} from "react-native-router-flux";
import styles from "../Styles";
import {Image, StyleSheet, View} from "react-native";
import Questions from "../questions";

export default class FifthView extends React.Component {

    items = ['1. Ognisko wulkaniczne', '2. Skała macierzysta', '3. Kanał lawowy', '4. Podnóże', '5. Sill',
        '6. Przewód boczny', '7. Warstwy popiołu emitowanego przez wulkan', '8. Zbocze', '9. Warstwy lawy emitowanej przez wulkan',
        '10. Gardziel', '11. Stożek pasożytniczy', '12. Potok lawowy', '13. Komin', '14. Krater', '15. Chmura popiołu'];

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


    renderQuizView(){
        return (
            <Container>
                <Content>
                    <Text style={styles.headerText}>Wulkany</Text>
                    <View style={innerStyle.viewStyle}>
                        <Image style={innerStyle.images} source={require('QuizApp/assets/budowaWulkanu.png')}/>
                        <Text style={styles.caption}>Wulkan</Text>
                    </View>
                    <List dataArray={this.items} renderRow={(item) =>
                        <ListItem>
                            <Text>{item}</Text>
                        </ListItem>
                    }>
                    </List>
                </Content>
                <Footer>
                    <Button light style={styles.bigButton} onPress={() => this.setState({testView: true})}>
                        <Text style={styles.bigButtonText}>Test</Text></Button>
                </Footer>
            </Container>
        )
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
        textAlign: 'center',
        color: 'white'
    }

    }
);