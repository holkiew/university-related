import * as React from "react";
import {hasRole} from "../../security/utils/TokenUtil";
import ROLES from "../../security/utils/Roles";
import {Button, Col, Row} from "reactstrap";
import ConfirmModal from "../../common/ConfirmModal";
import {CompanyDTO} from "./Companies";
import CompanyUserList from "./CompanyUserList";

interface CompanyDetailState {
    deleteModal: boolean
}

interface CompanyDetailProps {
    company: CompanyDTO
    editCompanyHandler(company: CompanyDTO): void
    deleteCompanyHandler(companyId?: number): void
}

export class CompanyDetail extends React.Component<CompanyDetailProps, CompanyDetailState> {
    constructor(props: CompanyDetailProps) {
        super(props);
        this.state = {
            deleteModal: false
        }
    }

    public render() {
        const { company } = this.props;
        return (<div className="card">
            <div className="card-header">
                <Row>
                    <Col xs="6">
                        <h3>Company detail</h3>
                    </Col>
                {hasRole(ROLES.ADMIN) &&
                    <Col xs="6">
                        <Button onClick={this.deleteCompanyConfirmHandler} color="danger" className="float-right">Delete</Button>
                        <Button onClick={() => this.props.editCompanyHandler(company)} color="primary" className="mr-2 float-right">Edit</Button>
                    </Col>
                }
                </Row>
            </div>
            <div className="card-body">
                <p>Phone: {company.id}</p>
                <p>Company: {company.name}</p>
                <p>Company: {company.address}</p>
                {hasRole(ROLES.ADMIN) && company.employees.length > 0 &&
                <CompanyUserList userList={company.employees}/>
                }
            </div>
            <ConfirmModal text={"Delete company '" + company.name + "'?"}
                          toggle={this.deleteCompanyConfirmHandler}
                          callback={() => this.props.deleteCompanyHandler(company.id)}
                          openModal={this.state.deleteModal}
                          confirmBtnText="Delete" />
        </div>);
    }

    private deleteCompanyConfirmHandler = () => {
        this.setState({
            deleteModal: !this.state.deleteModal
        });
    }
}
