import * as React from 'react';
import {RouteComponentProps} from 'react-router-dom'
import {Col, Row} from "reactstrap";
import EventList, {PageData} from "../eventProposal/components/EventList";
import axios from "axios";
import EventDetail from "../eventProposal/components/EventDetail";

interface HomepageState {
    pageData: PageData
    pageSize: number
    pageIndex: number
    loading: boolean
    selectedEvent: EventProposalDTO
}
export default class HomepagePanel extends React.Component<RouteComponentProps, HomepageState> {
    constructor(props: any) {
        super(props);
        this.state = {
            pageData: {
                proposalDTOList: [],
                totalPages: -1
            },
            loading: true,
            pageSize: 5,
            pageIndex: 0,
            selectedEvent: {} as EventProposalDTO,
        };
    }

    public render() {
        return (<div className="mt-3">
                <Row>
                    <Col xs="6">
                        <h1>Homepage</h1>
                    </Col>
                </Row>
                <hr />
                <Row className="col-12">
                    <Col md="6" xs="12">
                        <EventList {...this.state}
                                   onRowClick={this.eventSelectedHandler}
                                   fetchData={this.fetchEventProposals} />
                    </Col>
                    <Col md="6" xs="12">
                        {this.state.selectedEvent.id !== undefined &&
                        <EventDetail event={this.state.selectedEvent} />
                        }
                    </Col>
                </Row>
            </div>);
    }

    private eventSelectedHandler = (rowIndex: number) => {
        this.setState({selectedEvent: this.state.pageData.proposalDTOList && this.state.pageData.proposalDTOList[rowIndex]});
    };

    private fetchEventProposals = (page?: number, pageSize?: number) => {
        this.setState({
            loading: true,
        });
        const instance = axios.create();
        instance.get(`/api/event/getAllEvents`, {
            params: {
                page: page !== undefined ? page : this.state.pageIndex,
                pageSize: pageSize !== undefined ? pageSize : this.state.pageSize,
            }
        })
            .then(response => {
                this.setState({
                    pageData: response.data,
                    loading: false
                })
            })
            .catch(reason => {
                console.error(reason);
            })
    };

}
