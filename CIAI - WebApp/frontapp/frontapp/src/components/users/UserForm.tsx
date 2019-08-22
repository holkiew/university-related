import * as React from "react";
import {Alert, Button, Col, Container, Form, Input, Label} from "reactstrap";
import {UserDTO} from "./Users";
import FormGroup from "reactstrap/lib/FormGroup";
import ROLES from "../../security/utils/Roles";
import FormInput, {ValidationMessages} from "../../common/FormInput";
import {Redirect, RouteComponentProps} from "react-router";
import axios from "axios";
import {hasRole} from "../../security/utils/TokenUtil";


export interface UserFormState {
    user: UserDTO
    originalUser: UserDTO 
    userType?: ROLES
    admin: boolean
    validation: ValidationMessages
    submitted?: boolean
}

export default class UserForm extends React.Component<RouteComponentProps, UserFormState> {
    constructor(props: RouteComponentProps) {
        super(props);
        this.state = {
            user: this.props.location.state ? this.props.location.state.user : {
                firstname: "",
                surname: "",
                phone: "",
                isApproval: false,
                company: {
                    name: ""
                },
                authUser: {
                    username: "",
                    email: "",
                    roles: []
                }
            },
            originalUser: this.props.location.state ? this.props.location.state.user : undefined,
            admin: false,
            validation: {
                validationMessage: "",
                errors: []
            },
        };
    }

    public render() {
        const user = this.state.user;
        return (
            <Container className="mt-3">
                {this.redirectToUsers()}
                <div className="card">
                    <div className="card-header">{user ? "Edit user" : "New user"}</div>
                    <div className="card-body">
                        <Form>
                            {this.state.validation.validationMessage !== "" &&
                            <Alert color="danger">{this.state.validation.validationMessage}</Alert>
                            }
                            <FormGroup tag="fieldset" row>
                                <legend className="col-form-label col-sm-2">UserType</legend>
                                <Col sm={10}>
                                    <FormGroup check>
                                        <Input type="radio" name="userType" id="ecmaUser" defaultChecked={this.userIsInRole(user, ROLES.USER)}
                                               onChange={(e) => e.target.checked && this.setState({userType: ROLES.USER})} />
                                        <Label for="ecmaUser" check>ECMA user</Label>
                                    </FormGroup>
                                    <FormGroup check>
                                        <Input type="radio" name="userType" id="partnerUser" defaultChecked={this.userIsInRole(user, ROLES.PARTNER_USER)}
                                               onChange={(e) => e.target.checked && this.setState({userType: ROLES.PARTNER_USER})} />
                                        <Label for="partnerUser" check>Partner user</Label>
                                    </FormGroup>
                                </Col>
                            </FormGroup>
                            <FormInput id="username" name="username" label="Username" type="text" placeholder="Username" value={user ? user.authUser.username : ""}
                                       onchange={(e) => this.setState({user: {...user, authUser: {...user.authUser, username: e.target.value}}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="email" name="email" label="Email" type="email" placeholder="Email" value={user ? user.authUser.email : ""}
                                       onchange={(e) => this.setState({user: {...user, authUser: {...user.authUser, email: e.target.value}}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="password" name="password" label="Password" type="text" placeholder="" value={user ? user.authUser.password : ""}
                                       onchange={(e) => this.setState({user: {...user, authUser: {...user.authUser, password: e.target.value}}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="firstname" name="firstname" label="Firstname" type="text" placeholder="Firstname" value={user ? user.firstname : ""}
                                       onchange={(e) => this.setState({user: {...user, firstname: e.target.value}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="surname" name="surname" label="Surname" type="text" placeholder="Surname" value={user ? user.surname : ""}
                                       onchange={(e) => this.setState({user: {...user, surname: e.target.value}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="phoneNumber" name="phoneNumber" label="Phone" type="number" placeholder="Phone" value={user ? user.phone : ""}
                                       onchange={(e) => this.setState({user: {...user, phone: e.target.value}})}
                                       getValidationMessage={this.getValidationMessage} />
                            { this.hasPartnerUserRole() &&
                                <FormInput id="companyName" name="companyName" label="Company name" type="text" placeholder="Company name"
                                           value={user && user.company ? user.company.name : ""}
                                           onchange={(e) => this.setState({user: {...user, company: {...user.company, name: e.target.value}}})}
                                           getValidationMessage={this.getValidationMessage} />
                            }
                            <FormGroup tag="fieldset" row>
                                <Col sm={10}>
                                    <FormGroup check>
                                        <Input type="checkbox" name="admin" id="admin" defaultChecked={this.userIsInRole(user, ROLES.PARTNER_ADMIN) || this.userIsInRole(user, ROLES.ADMIN)}
                                               onChange={(e) => this.setState({admin: e.target.checked})}/>
                                        <Label for="admin" check>Administrator</Label>
                                    </FormGroup>
                                    {(user && this.hasEcmaUserRole()) &&
                                    <FormGroup check>
                                        <Input type="checkbox" name="isApproval" id="isApproval" defaultChecked={user && user.isApproval}
                                               onChange={(e) => this.setState({user: {...user, isApproval: e.target.checked}})} />
                                        <Label for="isApproval" check >IsApproval</Label>
                                    </FormGroup>
                                    }
                                </Col>
                            </FormGroup>
                            <Button onClick={this.state.originalUser ? this.updateUser : this.createNewUser}>Save</Button>
                        </Form>
                    </div>
                </div>
            </Container>);
    }

    private hasPartnerUserRole = () => {
        return this.userIsInRole(this.state.user, ROLES.PARTNER_USER) || this.state.userType === ROLES.PARTNER_USER;
    };

    private hasEcmaUserRole = () => {
        return this.userIsInRole(this.state.user, ROLES.USER) || this.state.userType === ROLES.USER;
    };

    private redirectToUsers = () => {
        if (this.state.submitted) {
            return <Redirect to="/users" />
        }
        return undefined;
    };

    private parseErrors = (validationResponse: any) => {
        const validation: ValidationMessages = {
            validationMessage: validationResponse.errors ? "" : validationResponse.message,
            errors: []
        };

        if (validationResponse.errors) {
            validationResponse.errors.forEach((error: any) => {
                const index = validation.errors.findIndex(e => e.field === error.field);
                if (index >= 0) {
                    validation.errors[index].messages.push(error.defaultMessage);
                }else {
                    validation.errors.push({
                        field: error.field,
                        messages: [error.defaultMessage]
                    });
                }
            });
        }
        this.setState({ validation: {...validation} });
    };

    private getValidationMessage = (id: string) => {
        const error = this.state.validation.errors.find(e => e.field === id);
        if (error) {
            return error.messages.join(' ');
        }
        return undefined;
    };

    private userIsInRole(user: UserDTO | undefined, role: ROLES) {
        return user && user.authUser.roles.find(r => r.name === role) !== undefined;
    };

    private normalizeForm = (event: any) => {
        if (!this.state.user) {
            return;
        }
        const user: UserDTO = {
            id: this.state.user.id,
            firstname: this.state.user.firstname,
            surname: this.state.user.surname,
            phone: this.state.user.phone,
            company: {
                name: this.state.user.company ? this.state.user.company.name : ""
            },
            isApproval: this.state.user.isApproval,
            authUser: {
                username: this.state.user.authUser.username,
                password: this.state.user.authUser.password,
                email: this.state.user.authUser.email,
                roles: []
            }
        };
        if (this.state.userType === ROLES.USER) {
            if (this.state.admin) {
                user.authUser.roles.push({ name: ROLES.ADMIN });
            }
            user.authUser.roles.push({ name: ROLES.USER });
        } else {
            if (this.state.admin) {
                user.authUser.roles.push({ name: ROLES.PARTNER_ADMIN });
            }
            user.authUser.roles.push({ name: ROLES.PARTNER_USER });
        }
        return user;
    };

    private createNewUser = (event: any) => {
        const user = this.normalizeForm(event);
        const instance = axios.create();
        let url = "";

        if (!user) {
            return;
        }
        if (user.authUser.roles.find(r => r.name === ROLES.USER)) {
            if (user.authUser.roles.find(r => r.name === ROLES.ADMIN)) {
                url = `/api/auth/signupUserAdmin`;
            } else {
                url = `/api/auth/signupUser`;
            }
            instance.post(url, {
                firstname: user.firstname,
                surname: user.surname,
                phoneNumber: user.phone,
                username: user.authUser.username,
                email: user.authUser.email,
                password: user.authUser.password
            })
            .then(response => {
                this.setState({submitted: true });
            })
            .catch(reason => {
                if (reason.response) {
                    this.parseErrors(reason.response.data);
                }
            });
        } else {
            if (user.authUser.roles.find(r => r.name === ROLES.PARTNER_ADMIN)) {
                url = `/api/auth/singupPartnerAdmin`;
            } else {
                url = `/api/auth/signupPartnerUser`;
            }
            instance.post(url, {
                firstname: user.firstname,
                surname: user.surname,
                companyName: user.company ? user.company.name : "",
                phoneNumber: user.phone,
                username: user.authUser.username,
                email: user.authUser.email,
                password: user.authUser.password
            })
            .then(response => {
                this.setState({submitted: true });
            })
            .catch(reason => {
                if (reason.response) {
                    this.parseErrors(reason.response.data);
                }
            });
        }
    };

    private updateUser = (event: any) => {
        const originalUser = this.state.originalUser;
        const newUser = this.normalizeForm(event);
        const instance = axios.create();
        if (!originalUser || !newUser) {
            console.error("Missing original user for edit form!");
            return;
        }
        const url = hasRole(ROLES.ADMIN) || hasRole(ROLES.PARTNER_ADMIN) ?
            `/api/partner/manageUserAccountAdmin` :
            `/api/partner/manageUserAccount`;
        instance.post(url, {
            userCompanyName: originalUser.company ? originalUser.company.name : "",
            username: originalUser.authUser.username,
            newUsername: newUser.authUser.username,
            newCompany: newUser.company ? newUser.company.name : "",
            newPassword: newUser.authUser.password,
            firstname: newUser.firstname,
            surname: newUser.surname,
            phoneNumber: newUser.phone,
            email: newUser.authUser.email,
            isApproval: newUser.isApproval
        })
        .then(response => {
            this.setState({submitted: true });
        })
        .catch(reason => {
            if (reason.response) {
                this.parseErrors(reason.response.data);
            }
        })
    };
}
