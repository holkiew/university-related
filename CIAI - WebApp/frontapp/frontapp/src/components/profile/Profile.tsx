import * as React from 'react';
import axios from "axios";
import {Button, Card, CardBody, CardHeader, Col, Container, Row} from "reactstrap";
import {Redirect, RouteComponentProps} from "react-router";
import {UserDTO} from "../users/Users";
import {removeToken} from "../../security/utils/TokenUtil";
import {AuthUserDetail} from "../users/UserDetail";

export interface ProfileState {
    user?: UserDTO
    updateUser?: boolean
}

export default class ProfilePanel extends React.Component<RouteComponentProps, ProfileState> {
    constructor(props: RouteComponentProps) {
        super(props);
        this.state = {};
    }

    public componentWillMount() {
        this.loadUser();
    }

    public render() {
        const user = this.state.user;
        if (!user) {
            return <Container className="mt-3">
                <Card>
                    <CardHeader>
                        <Row>
                            <Col xs="6">
                                <h3>My profile</h3>
                            </Col>
                        </Row>
                    </CardHeader>
                    <CardBody>Loading...</CardBody>
                </Card>
            </Container>
        }
        return (<Container className="mt-3">
            { this.state.updateUser &&
            <Redirect to={{pathname: "/user/edit/" + user.id, state: { user: this.state.user}}} />
            }
            <Card>
                <CardHeader>
                    <Row>
                        <Col xs="6">
                            <h3>My profile</h3>
                        </Col>
                        <Col xs="6">
                            <Button onClick={this.deleteUserHandler} color="danger" className="float-right">Delete</Button>
                            <Button onClick={this.editUserHandler} color="primary" className="mr-2 float-right">Edit</Button>
                        </Col>
                    </Row>
                </CardHeader>
                <CardBody>
                    <p>Name: {user.firstname + ' ' + user.surname}</p>
                    {user.isApproval &&
                    <p><b>*Approval account*</b></p>
                    }
                    <p>Phone: {user.phone}</p>
                    <p>Company: {user.company ? user.company.name : "ECMA"}</p>
                    <AuthUserDetail user={user.authUser} />
                </CardBody>
            </Card>
        </Container>);
    }

    private loadUser = () => {
        const instance = axios.create();
        instance.get(`/api/partner/getUserProfile`)
            .then(response => {
                this.setState({
                    user: response.data
                });
            })
            .catch(reason => {
                console.error(reason);
            })
    };

    private editUserHandler = () => {
        this.setState({
            updateUser: true
        });
    };

    private deleteUserHandler = () => {
        const instance = axios.create();
        instance.delete(`/api/partner/deleteUser`, {
            params: { userId: this.state.user && this.state.user.id }
        })
        .then(response => {
            removeToken();
            this.props.history.push('/');
        })
        .catch(reason => {
            console.error(reason);
        })
    };
}