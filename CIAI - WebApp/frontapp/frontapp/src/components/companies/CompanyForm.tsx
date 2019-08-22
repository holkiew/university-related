import * as React from "react";
import {Alert, Button, Container, Form} from "reactstrap";
import FormInput, {ValidationMessages} from "../../common/FormInput";
import {Redirect, RouteComponentProps} from "react-router";
import axios from "axios";
import {CompanyDTO} from "./Companies";


export interface CompanyFormState {
    company: CompanyDTO
    originalCompany: CompanyDTO
    validation: ValidationMessages
    submitted?: boolean
}

export default class CompanyForm extends React.Component<RouteComponentProps, CompanyFormState> {
    constructor(props: RouteComponentProps) {
        super(props);
        this.state = {
            company: this.props.location.state ? this.props.location.state.company : {
                name: "",
                address: "",
                employees: []
            },
            originalCompany: this.props.location.state ? this.props.location.state.company : undefined,
            validation: {
                validationMessage: "",
                errors: []
            },
        };
    }

    public render() {
        const company = this.state.company;
        return (
            <Container className="mt-3">
                {this.redirectToCompanies()}
                <div className="card">
                    <div className="card-header">{company ? "Edit company" : "New company"}</div>
                    <div className="card-body">
                        <Form>
                            {this.state.validation.validationMessage !== "" &&
                            <Alert color="danger">{this.state.validation.validationMessage}</Alert>
                            }
                            <FormInput id="name" name="name" label="Name" type="text" placeholder="Name" value={company ? company.name : ""}
                                       onchange={(e) => this.setState({company: {...company, name: e.target.value}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <FormInput id="address" name="address" label="Address" type="text" placeholder="Address" value={company ? company.address : ""}
                                       onchange={(e) => this.setState({company: {...company, address: e.target.value}})}
                                       getValidationMessage={this.getValidationMessage} />
                            <Button onClick={this.state.originalCompany ? this.updateCompany : this.createNewCompany}>Save</Button>
                        </Form>
                    </div>
                </div>
            </Container>);
    }

    private redirectToCompanies = () => {
        if (this.state.submitted) {
            return <Redirect to="/companies" />
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

    private normalizeForm = (event: any) => {
        if (!this.state.company) {
            return;
        }
        return {
            id: this.state.company.id,
            name: this.state.company.name,
            address: this.state.company.address,
            employees: []
        };
    };

    private createNewCompany = (event: any) => {
        const company = this.normalizeForm(event);
        const instance = axios.create();
        if (!company) {
            return;
        }
        instance.post(`/api/company/addNewCompany`, {
            name: company.name,
            address: company.address,
        })
        .then(response => {
            this.setState({submitted: true });
        })
        .catch(reason => {
            if (reason.response) {
                this.parseErrors(reason.response.data);
            }
        });
    };

    private updateCompany = (event: any) => {
        const originalCompany = this.state.originalCompany;
        const newCompany = this.normalizeForm(event);
        const instance = axios.create();

        if (!originalCompany || !newCompany) {
            console.error("Missing original company for edit form!");
            return;
        }

        instance.patch(`/api/company/manageCompany`, {
            name: originalCompany.name,
            newName: newCompany.name,
            newAddress: newCompany.address,
        })
        .then(response => {
            this.setState({submitted: true });
        })
        .catch(reason => {
            if (reason.response) {
                this.parseErrors(reason.response.data);
            }
        });
    };
}
