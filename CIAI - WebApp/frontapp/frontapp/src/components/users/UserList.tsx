import * as React from 'react';
import ReactTable, {RowInfo} from "react-table";

import {UserDTO} from "./Users";
import axios from "axios";

export interface UserListProps {
    reload: boolean
    onListLoad(users: UserDTO[]): void
    onRowClick(index: number): void
}

export interface UserListState {
    loading: boolean
    pageSize: number
    pageData?: PageData
}

interface PageData {
    userDTOList: UserDTO[]
    totalPages: number
}

export default class UserList extends React.Component<UserListProps, UserListState> {
    constructor(props: UserListProps) {
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
                        data={this.state.pageData && this.state.pageData.userDTOList}
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
                        getTdProps={(state: UserListState, rowInfo: RowInfo) => {
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
        instance.get(`/api/partner/getAllUsers`, {
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
                this.props.onListLoad(response.data && response.data.userDTOList);
            })
            .catch(reason => {
                console.error(reason);
            })
    }
}

const tableColumns = [{
    Header: 'Surname',
    accessor: 'surname',
}, {
    Header: 'FirstName',
    accessor: 'firstname'
}, {
    Header: 'Company',
    accessor: 'company.name',
    Cell: (row: any) => (<span>{row.value !== undefined ? row.value : "ECMA"}</span>)
},{
    Header: 'Approval',
    accessor: 'isApproval',
    Cell: (row: any) => (<span>{row.value === true ? "Yes" : "No"}</span>)
}];
