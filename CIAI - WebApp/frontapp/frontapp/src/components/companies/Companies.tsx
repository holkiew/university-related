import * as React from 'react';
import axios from "axios";
import {Button, Col, Row} from "reactstrap";
import {CompanyDetail} from "./CompanyDetail";
import {Redirect, RouteComponentProps} from "react-router";
import CompanyList from "./CompanyList";

export interface CompanyState {
    companyList?: CompanyDTO[]
    selectedCompany?: CompanyDTO
    createNewCompany?: boolean
    updateCompany?: boolean
    reloadCompanyList: boolean
}

export interface CompanyDTO {
    id?: number
    name: string
    address: string
    employees: CompanyUserDTO[]
}

export interface CompanyUserDTO {
    id?: number
    firstname: string
    surname: string
    isApproval: boolean
}

export default class CompaniesPanel extends React.Component<RouteComponentProps, CompanyState> {
    constructor(props: RouteComponentProps) {
        super(props);
        this.state = { reloadCompanyList: false };
    }

    public componentWillMount() {
        this.editCompanyHandler.bind(this);
        this.deleteCompanyHandler.bind(this);
    }

    public componentDidUpdate() {
        if (this.state.reloadCompanyList) {
            this.setState({reloadCompanyList: false});
        }
    }

    public render() {
        return (<div className="mt-3">
            { this.state.createNewCompany && <Redirect to="/company/new" /> }
            { this.state.updateCompany && this.state.selectedCompany &&
            <Redirect to={{pathname: "/company/edit/" + this.state.selectedCompany.id, state: { company: this.state.selectedCompany } }} />
            }
            <Row>
                <Col xs="6">
                    <h1 >Companies</h1>
                </Col>
                <Col xs="6">
                    <Button onClick={this.newCompanyHandler} color="primary" className="mr-2 float-right">New Company</Button>
                </Col>
            </Row>
            <hr />
            <Row className="col-12">
                <Col md="6" xs="12">
                    <CompanyList onRowClick={this.companySelectedHander} onListLoad={this.companyListLoaded} reload={this.state.reloadCompanyList} />
                </Col>
                { this.state.selectedCompany &&
                <Col md="6" xs="12">
                    <CompanyDetail company={this.state.selectedCompany} editCompanyHandler={this.editCompanyHandler} deleteCompanyHandler={this.deleteCompanyHandler} />
                </Col>
                }
            </Row>
        </div>);
    }

    private companyListLoaded = (companies: CompanyDTO[]) => {
        this.setState({companyList: companies});
    };

    private companySelectedHander = (rowIndex: number) => {
        this.setState({selectedCompany: this.state.companyList && this.state.companyList[rowIndex]});
    };

    private newCompanyHandler = () => {
        this.setState({
            createNewCompany: true
        });
    };

    private editCompanyHandler = () => {
        this.setState({
            updateCompany: true
        });
    };

    private deleteCompanyHandler = (companyId: number) => {
        const instance = axios.create();
        instance.delete(`/api/company/deleteCompany`, {
            params: { companyId }
        })
        .then(response => {
            this.setState({reloadCompanyList: true});
            this.hideCompanyDetail();
        })
        .catch(reason => {
            console.error(reason);
        })
    };

    private hideCompanyDetail = () => {
        this.setState({ selectedCompany: undefined });
    };
}