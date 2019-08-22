import * as React from 'react';
import ReactTable, {RowInfo} from "react-table";
import axios from "axios";
import {CompanyDTO} from "./Companies";

interface CompanyListProps {
    reload: boolean
    onListLoad(companies: CompanyDTO[]): void
    onRowClick(index: number): void
}

export interface CompanyListState {
    loading: boolean
    pageSize: number
    pageData?: PageData
}

interface PageData {
    companyDTOList: CompanyDTO[]
    totalPages: number
}

export default class CompanyList extends React.Component<CompanyListProps, CompanyListState> {
    constructor(props: CompanyListProps) {
        super(props);
        this.state = {
            loading: true,
            pageSize: 10
        };
    }

    public componentDidUpdate() {
        if (this.props.reload) {
            this.fetchUsers(0, this.state.pageSize);
        }
    }

    public render() {
        return (<div>
            <ReactTable manual
                        data={this.state.pageData && this.state.pageData.companyDTOList}
                        pages={this.state.pageData && this.state.pageData.totalPages}
                        columns={tableColumns}
                        className="-highlight"
                        onPageSizeChange={(pageSize, pageIndex) => {
                            this.setState({pageSize});
                        }}
                        loading={this.state.loading}
                        defaultPageSize={this.state.pageSize}
                        onFetchData={(state, instance) => {
                            this.fetchUsers(state.page, state.pageSize);
                        }}
                        getTdProps={(state: CompanyListState, rowInfo: RowInfo) => {
                            return {
                                onClick: (e: any, handleOriginal: any) => {
                                    if (rowInfo !== undefined) {
                                        this.props.onRowClick(rowInfo.index);
                                    }
                                    if (handleOriginal) {
                                        handleOriginal();
                                    }
                                }
                            };
                        }} />
        </div>);
    }

    private fetchUsers = (page: number, pageSize: number) => {
        this.setState({
            loading: true
        });
        const instance = axios.create();
        instance.get(`/api/company/getAllCompanies`, {
            params: {
                page,
                pageSize,
            }
        })
            .then(response => {
                this.setState({
                    pageData: response.data,
                    loading: false
                });
                this.props.onListLoad(response.data && response.data.companyDTOList);
            })
            .catch(reason => {
                console.error(reason);
            })
    }
}

const tableColumns = [{
    Header: 'Name',
    accessor: 'name',
}, {
    Header: 'Address',
    accessor: 'address'
}];
