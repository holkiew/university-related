import * as React from 'react';
import {Button} from "reactstrap";
import ReactTable from "react-table";
import {MdCheck, MdClose} from "react-icons/md";
import {hasRole} from "../../../security/utils/TokenUtil";
import ROLES from "../../../security/utils/Roles";

interface EventListProps {
    pageData: PageData
    loading: boolean
    pageSize: number
    pageIndex: number
    onRowClick(index: number): void
    showUpdateEventProposalModal?(event: EventProposalDTO): void
    showDeleteEventProposalModal?(event: EventProposalDTO): void
    fetchData(page: number, pageSize: number): void
}

export interface EventListState {
    proposalDTOList: EventProposalDTO[]
}

export interface PageData {
    proposalDTOList: EventProposalDTO[]
    totalPages: number
}


export default class EventList extends React.Component<EventListProps, EventListState> {
    public state = {
        proposalDTOList: this.props.pageData.proposalDTOList
    };

    private tableColumns = [{
        Header: 'Title',
        accessor: 'title',
    }, {
        Header: 'Budget',
        accessor: 'budget'
    }, {
        Header: 'Description',
        accessor: 'description',
    }, {
        Header: 'Goals',
        accessor: 'goals',
    }, {
        Header: 'Needed materials',
        accessor: 'neededMaterials',
    }, {
        Header: 'Work plan',
        accessor: 'workPlan',
    }, {
        Header: 'Is approved',
        Cell: (instance: any) => (instance.original.approved ? <MdCheck/> : <MdClose/>)
    }] as any;

    constructor(props: EventListProps) {
        super(props);
    }

    public componentDidMount() {
        if (hasRole(ROLES.USER) || hasRole(ROLES.ADMIN)) {
            this.tableColumns.push({
                id: 'update_row',
                Cell: (instance: any) => (<Button onClick={() => {
                    if (this.props.showUpdateEventProposalModal !== undefined) {
                        this.props.showUpdateEventProposalModal(instance.original)
                    }
                }} color="info" size="sm">Edit</Button>)
            });
            if (hasRole(ROLES.ADMIN)) {
                this.tableColumns.push({
                    id: 'delete_row',
                    Cell: (instance: any) => (
                        <Button onClick={() =>{
                            if (this.props.showDeleteEventProposalModal !== undefined) {
                                this.props.showDeleteEventProposalModal(instance.original)
                            }
                        }} color="danger" size="sm">Delete</Button>)
                })
            }
        }
    }

    public render() {
        return (<ReactTable className="-highlight"
                            manual
                            data={this.props.pageData.proposalDTOList}
                            pages={this.props.pageData.totalPages}
                            columns={this.tableColumns}
                            onPageSizeChange={(pageSize, pageIndex) => {
                                // this.setState({pageSize, pageIndex});
                            }}
                            onPageChange={(pageIndex) => {
                                // this.setState({pageIndex});
                            }}
                            loading={this.props.loading}
                            defaultPageSize={this.props.pageSize}
                            onFetchData={(state, instance) => {
                                this.props.fetchData(state.page, state.pageSize);
                            }}
                            sortable={false}
                            getTdProps={(state: any, rowInfo: any, column: any, instance: any) => {
                                return {onClick: (e: any, handleOriginal: any): any => {
                                        if (rowInfo !== undefined && rowInfo.original !== undefined) {
                                            this.props.onRowClick(rowInfo.index)
                                        }
                                        if (handleOriginal) {
                                            handleOriginal();
                                        }
                                    } }
                            }} />);
    }
}
