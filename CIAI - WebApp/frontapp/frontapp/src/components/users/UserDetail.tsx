import * as React from "react";
import {hasRole} from "../../security/utils/TokenUtil";
import ROLES from "../../security/utils/Roles";
import {Button, Col, Row} from "reactstrap";
import {AuthUserDTO, UserDTO} from "./Users";
import ConfirmModal from "../../common/ConfirmModal";

interface UserDetailState {
    deleteModal: boolean
}

interface UserDetailProps {
    user: UserDTO
    editUserHandler(): void
    deleteUserHandler(userId?: number): void
}

export class UserDetail extends React.Component<UserDetailProps, UserDetailState> {
    constructor(props: UserDetailProps) {
        super(props);
        this.state = {
            deleteModal: false
        }
    }

    public render() {
        const { user } = this.props;
        return (<div className="card">
            <div className="card-header">
                <Row>
                    <Col xs="6">
                        <h3>User detail</h3>
                    </Col>
                {hasRole(ROLES.ADMIN) &&
                    <Col xs="6">
                        <Button onClick={this.deleteUserConfirmHandler} color="danger" className="float-right">Delete</Button>
                        <Button onClick={() => this.props.editUserHandler()} color="primary" className="mr-2 float-right">Edit</Button>
                    </Col>
                }
                </Row>
            </div>
            <div className="card-body">
                {hasRole(ROLES.ADMIN) &&
                <AuthUserDetail user={user.authUser}/>
                }
                <p>Name: {user.firstname} {user.surname}</p>
                <p>Company: {(user.company !== null && user.company !== undefined) ? user.company.name : "ECMA"}</p>
                <p>Phone: {user.phone}</p>
            </div>
            <ConfirmModal text={"Delete user '" + user.authUser.username + "'?"}
                          toggle={this.deleteUserConfirmHandler}
                          callback={() => this.props.deleteUserHandler(user.id)}
                          openModal={this.state.deleteModal}
                          confirmBtnText="Delete" />
        </div>);
    }

    private deleteUserConfirmHandler = () => {
        this.setState({
            deleteModal: !this.state.deleteModal
        });
    }
}

export const AuthUserDetail = (props: { user: AuthUserDTO }) => {
    return (<div>
        <p>Username: {props.user.username}</p>
        <p>Email: {props.user.email}</p>
        <p>Roles:</p>
        <ul>{props.user.roles.map((c, i) => (<li key={i}>{c.name}</li>))}</ul>
    </div>);
};
