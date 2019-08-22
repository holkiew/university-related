import * as React from 'react';
import axios from "axios";
import {Button, Col, Row} from "reactstrap";
import {UserDetail} from "./UserDetail";
import {Redirect, RouteComponentProps} from "react-router";
import UserList from "./UserList";

export interface UsersState {
    userList?: UserDTO[]
    selectedUserId?: number
    createNewUser?: boolean
    updateUser?: boolean
    reloadUserList: boolean
}

export default class UsersPanel extends React.Component<RouteComponentProps, UsersState> {
    constructor(props: RouteComponentProps) {
        super(props);
        this.state = { reloadUserList: false };
    }

    public componentWillMount() {
        this.editUserHandler.bind(this);
        this.deleteUserHandler.bind(this);
    }

    public componentDidUpdate() {
        if (this.state.reloadUserList) {
            this.setState({reloadUserList: false});
        }
    }

    public render() {
        const selectedUserRowId = this.state.userList ? this.state.userList.findIndex((u) => u.id === this.state.selectedUserId) : -1;
        return (<div className="mt-3">
            { this.state.createNewUser && <Redirect to="/user/new" /> }
            { this.state.updateUser && this.state.userList && selectedUserRowId !== -1 &&
            <Redirect to={{pathname: "/user/edit/" + this.state.selectedUserId, state: { user: this.state.userList[selectedUserRowId]}}} />
            }
            <Row>
                <Col xs="6">
                    <h1 >Users</h1>
                </Col>
                <Col xs="6">
                    <Button onClick={this.newUserHandler} color="primary" className="mr-2 float-right">New User</Button>
                </Col>
            </Row>
            <hr />
            <Row className="col-12">
                <Col md="6" xs="12">
                    <UserList onRowClick={this.userSelectedHander} onListLoad={this.userListLoaded} reload={this.state.reloadUserList} />
                </Col>
                { this.state.userList && selectedUserRowId !== -1 &&
                <Col md="6" xs="12">
                    <UserDetail user={this.state.userList[selectedUserRowId]}
                                editUserHandler={this.editUserHandler} deleteUserHandler={this.deleteUserHandler} />
                </Col>
                }
            </Row>
        </div>);
    }

    private userListLoaded = (users: UserDTO[]) => {
        this.setState({userList: users});
        if (this.props.match.params) {
            const { userId } = this.props.match.params as { userId: string };
            if (!isNaN(Number(userId))) {
                this.setState({ selectedUserId: Number(userId) })
            }
        }
    };

    private userSelectedHander = (rowIndex: number) => {
        const userId = this.state.userList && this.state.userList[rowIndex].id;
        this.props.history.push("/users/" + userId);
        this.setState({selectedUserId: userId});
    };

    private newUserHandler = () => {
        this.setState({
            createNewUser: true
        });
    };

    private editUserHandler = () => {
        this.setState({
            updateUser: true
        });
    };

    private deleteUserHandler = (userId: number) => {
        const instance = axios.create();
        instance.delete(`/api/partner/deleteUser`, {
            params: { userId }
        })
        .then(response => {
            this.setState({reloadUserList: true});
            this.hideUserDetail();
        })
        .catch(reason => {
            console.error(reason);
        })
    };

    private hideUserDetail = () => {
        this.setState({ selectedUserId: undefined });
    };
}

export interface UserDTO {
    id?: number
    company?: CompanyBasicInfoDTO
    firstname: string
    surname: string
    phone: string
    isApproval: boolean
    authUser: AuthUserDTO
    eventProposals?: EventProposalDTO[]
    eventProposalReviewAssignments?: EventProposalDTO[]
    reviews?: EventProposalReviewDTO[]
}

export interface CompanyBasicInfoDTO {
    id?: number
    name?: string
    address?: string
}

interface EventProposalDTO {
    budget: number
    description: string
    goals: string
    id: number
    neededMaterials: string
    title: string
    workPlan: string
}
interface EventProposalReviewDTO {
    id: number
    reviewText: string
    approved: boolean
}

export interface AuthUserDTO {
    id?: number
    username: string
    password?: string
    email: string
    roles: RoleDTO[]
}


interface RoleDTO {
    id?: number
    name: string
}
