import * as React from 'react';
import * as env from "config.json";
import {Alert, Button, Card, CardBody, CardHeader, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import axios from "axios";
import {RouteComponentProps} from 'react-router-dom';
import {removeToken, setToken} from "security/utils/TokenUtil";

enum LOGIN_STATE {INITIAL, LOGIN_SUCCESSFUL, LOGIN_FAILED}

interface LoginState {
    loginState: LOGIN_STATE,
    errorReason: any,
    loginInput: string,
    passwordInput: string
}

export default class Login extends React.Component<RouteComponentProps, LoginState> {
    public readonly state = {
        loginState: LOGIN_STATE.INITIAL,
        errorReason: "empty",
        loginInput: "",
        passwordInput: ""
    };

    public render() {
        return (
            <Container className="mt-3 col-xs-12 col-sm-6 col-lg-3">
                <Card>
                    <CardHeader>
                        <h3>Login</h3>
                    </CardHeader>
                    <CardBody>
                        <Form onSubmit={this.handleOnSubmitLogin}>
                            <FormGroup>
                                <Label for="login">Login</Label>
                                <Input type="text" name="login" id="login" placeholder="email or login"
                                       onChange={(e) => this.setState({loginInput: e.target.value})}/>
                            </FormGroup>
                            <FormGroup>
                                <Label for="password">Password</Label>
                                <Input type="password" name="password" id="password" placeholder="password"
                                       onChange={(e) => this.setState({passwordInput: e.target.value})}/>
                            </FormGroup>
                            {this.renderAfterLoginMessage()}
                        </Form>
                        <Button onClick={this.login} className="primary float-right">Login</Button>
                    </CardBody>
                </Card>
            </Container>
        );
    }

    private renderAfterLoginMessage = () => {
        switch (this.state.loginState) {
            case LOGIN_STATE.LOGIN_SUCCESSFUL:
                setTimeout(() => this.props.history.push("/"), 1000);
                return <Alert className="success">Login successful</Alert>;
            case LOGIN_STATE.LOGIN_FAILED:
                return <Alert className="danger">Login LOGIN_FAILED {this.state.errorReason}</Alert>;
            default:
                return;
        }
    };

    private handleOnSubmitLogin = (event: any) => {
        event.preventDefault();
        this.login();
    };

    private login = () => {
        axios.post(`${env.backendServer.baseUrl}/api/auth/signin`, {
            password: this.state.passwordInput,
            usernameOrEmail: this.state.loginInput
        })
            .then(res => {
                setToken(res.data.accessToken);
                this.setState({
                    loginState: LOGIN_STATE.LOGIN_SUCCESSFUL
                })
            })
            .catch(reason => {
                removeToken();
                this.setState({
                    loginState: LOGIN_STATE.LOGIN_FAILED,
                    errorReason: ""
                })
            })
    }
}
