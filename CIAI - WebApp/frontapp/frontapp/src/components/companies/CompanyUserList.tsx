import * as React from "react";
import {ListGroup, ListGroupItem} from "reactstrap";
import {CompanyUserDTO} from "./Companies";
import {Redirect} from "react-router";

interface CompanyUserListState {
    redirectToUserDetail: boolean
    selectedUserId?: number
}

interface CompanyUserListProps {
    userList: CompanyUserDTO[]
}

export default class CompanyUserList extends React.Component<CompanyUserListProps, CompanyUserListState> {
    constructor(props: CompanyUserListProps) {
        super(props);
        this.state = {
            redirectToUserDetail: false
        }
    }

    public render() {
        return (<div>
            {this.state.redirectToUserDetail && this.state.selectedUserId && <Redirect to={"/users/" + this.state.selectedUserId}/>}
            <h3>Users </h3>
            <ListGroup>
                {this.props.userList.map((user, index) => {
                    return <ListGroupItem key={index} onClick={() => this.selectUserHandler(user.id)} action>
                        {user.firstname + ' ' + user.surname}
                    </ListGroupItem>
                })}
            </ListGroup>
        </div>);
    }

    private selectUserHandler(userId: number | undefined) {
        if (userId) {
            this.setState({
                selectedUserId: userId,
                redirectToUserDetail: true
            });
        }
    }
};
