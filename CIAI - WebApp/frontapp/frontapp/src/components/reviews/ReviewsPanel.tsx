import * as React from 'react';
import {RouteComponentProps} from 'react-router-dom'
import {Button, Col, Row} from "reactstrap";
import EventList, {PageData} from "../eventProposal/components/EventList";
import axios from "axios";
import EventDetail from "../eventProposal/components/EventDetail";
import ReviewAddModal from "./ReviewAddModal";

interface ReviewsState {
    pageData: PageData
    pageSize: number
    pageIndex: number
    loading: boolean
    selectedEvent: EventProposalDTO
    openReviewModal: boolean
}

export default class ReviewsPanel extends React.Component<RouteComponentProps, ReviewsState> {
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
            openReviewModal: false
        };
    }

    public render() {
        return (<div className="mt-3">
            {this.showReviewModal()}
            <Row>
                <Col xs="6">
                    <h1>My assigned reviews</h1>
                </Col>
            </Row>
            <hr/>
            <Row className="col-12">
                <Col md="6" xs="12">
                    <EventList {...this.state}
                               onRowClick={this.eventSelectedHandler}
                               fetchData={this.fetchEventProposalsToReview}/>
                </Col>
                {this.state.selectedEvent.id !== undefined &&
                <Col md="6" xs="12">
                    <Button onClick={this.addReview} className="mr-2 col-12 btn-success">Add Review</Button>
                    <EventDetail event={this.state.selectedEvent}/>
                </Col>
                }
            </Row>
        </div>);
    }

    private showReviewModal = () => {
        if (this.state.openReviewModal) {
            return <ReviewAddModal onAddCallback={this.onModalDataUpdate} onCancel={this.onModalCancel}
                                   eventProposalId={this.state.selectedEvent.id}/>;
        }
        return "";
    }

    private addReview = () => {
        this.setState({openReviewModal: true});
    }

    private onModalCancel = () => {
        this.setState({openReviewModal: false});
    };

    private onModalDataUpdate = (success: boolean) => {
        if (success) {
            this.fetchEventProposalsToReview(this.state.pageIndex, this.state.pageSize);
            this.setState({selectedEvent: {} as EventProposalDTO});
        }
        this.setState({openReviewModal: false});
    };

    private eventSelectedHandler = (rowIndex: number) => {
        this.setState({selectedEvent: this.state.pageData.proposalDTOList && this.state.pageData.proposalDTOList[rowIndex]});
    };

    private fetchEventProposalsToReview = (page?: number, pageSize?: number) => {
        this.setState({
            loading: true,
        });
        const instance = axios.create();
        instance.get(`/api/eventProposalReview/getUsersProposalsToReview`, {
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
